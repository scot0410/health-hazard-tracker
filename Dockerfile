# --- STAGE 1: Build the application using Corretto 21 JDK ---
FROM amazoncorretto:21-alpine3.23 AS builder
WORKDIR /build

# Copy the Gradle wrapper files and configuration first to leverage Docker layer caching
COPY gradle/ ./gradle/
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# Copy your source code
COPY src ./src

# Grant execution permissions to the Gradle wrapper and compile the bootJar
# Skipping tests speeds up your build process significantly
RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon

# --- STAGE 2: Run the application on a fresh, clean Corretto 21 instance ---
FROM amazoncorretto:21-alpine3.23
WORKDIR /app

# Copy the built JAR file from the builder stage
# Spring Boot's Gradle plugin outputs the executable JAR to build/libs/
COPY --from=builder /build/build/libs/*.jar app.jar

# Expose standard Spring Boot web traffic port
EXPOSE 8080

# Run the container as a non-root user for enhanced AWS production security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]