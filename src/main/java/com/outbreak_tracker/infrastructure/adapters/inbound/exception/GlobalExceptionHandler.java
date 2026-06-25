package com.outbreak_tracker.infrastructure.adapters.inbound.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.concurrent.CompletionException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException ex) {
        Throwable rootCause = ex.getCause();

        return switch(rootCause) {
            case HttpClientErrorException clientEx -> buildErrorResponse(clientEx.getStatusCode().value(), "External Client Error", clientEx.getMessage());
            case HttpServerErrorException clientEx -> buildErrorResponse(clientEx.getStatusCode().value(), "External Server Error", clientEx.getMessage());
            default -> throw new IllegalStateException("Unexpected value: " + rootCause);
        };
    }

    ResponseEntity<ErrorResponse> buildErrorResponse(int status,
                                                     String error,
                                                     String message) {
        var response = ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(status)
                .error(error)
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }

}
