package com.health_hazard_tracker.infrastructure.adapters.outbound.recall;

import com.health_hazard_tracker.domain.model.FoodRecall;
import com.health_hazard_tracker.domain.model.ProductDescription;
import com.health_hazard_tracker.domain.ports.outbound.RecallClientPort;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client.FsisHttpClient;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client.FdaHttpClient;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallDto;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FsisRecallDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class RecallAdapter implements RecallClientPort {
    private final FdaHttpClient fdaHttpClient;
    private final FsisHttpClient fsisHttpClient;
    private final ExecutorService virtualExecutor;

    public RecallAdapter(
            FdaHttpClient fdaHttpClient, FsisHttpClient fsisHttpClient,
            @Qualifier("virtualExecutor") ExecutorService virtualExecutor) {
        this.fdaHttpClient = fdaHttpClient;
        this.fsisHttpClient = fsisHttpClient;
        this.virtualExecutor = virtualExecutor;
    }

    @Override
    public @Nullable List<FoodRecall> fetchLatestRecalls() {

        CompletableFuture<FdaRecallResponseDto> openFdaRecallFuture = CompletableFuture
                .supplyAsync(() -> Optional.ofNullable(fdaHttpClient.fetchOngoingRecallData())
                                .orElseGet(() -> FdaRecallResponseDto.builder().build()),
                        virtualExecutor)
                .exceptionally(throwable -> {
                    log.error("Error occurred while fetching OpenFDA data - {}", throwable.getMessage());
                    return FdaRecallResponseDto.builder().build();
                });

        CompletableFuture<List<FsisRecallDto>> fsisRecallFuture = CompletableFuture
                .supplyAsync(() -> Optional.ofNullable(fsisHttpClient.fetchRecallData())
                                .orElseGet(() -> List.of(FsisRecallDto.builder().build())),
                        virtualExecutor)
                .exceptionally(throwable -> {
                    log.error("Error occurred while fetching FSIS data - {}", throwable.getMessage());
                    return List.of(FsisRecallDto.builder().build());
                });

        CompletableFuture.allOf(openFdaRecallFuture, fsisRecallFuture).join();

        var fdaRecallDto = openFdaRecallFuture.join();
        var fsisRecallDto = fsisRecallFuture.join();

        return buildFoodRecall(fdaRecallDto.results(), fsisRecallDto);
    }

    private static List<FoodRecall> buildFoodRecall(List<FdaRecallDto> fdaRecallDtos,
                                                    List<FsisRecallDto> fsisRecallDtos) {

        var fdaRecalls = fdaRecallDtos.stream().map(RecallAdapter::fdaRecallMapper).toList();
        var fsisRecalls = fsisRecallDtos.stream().map(RecallAdapter::fsisRecallMapper).toList();
        return Stream.concat(fdaRecalls.stream(), fsisRecalls.stream()).toList();
    }

    private static FoodRecall fdaRecallMapper(FdaRecallDto fdaRecallDto) {
        var reportDate = fdaRecallDto.reportDate();
        var parsedDate = reportDate != null
                ? LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
                : null;

        var simpleLocation = String.format("%s, %s, %s", fdaRecallDto.city(), fdaRecallDto.state(), fdaRecallDto.country());

        var productDescription = buildProductDescription(fdaRecallDto);
        return FoodRecall.builder()
                .recallNumber(fdaRecallDto.recallNumber())
                .recallingFirm(fdaRecallDto.recallingFirm())
                .productDescription(productDescription)
                .reasonForRecall(fdaRecallDto.reasonForRecall())
                .classification(fdaRecallDto.classification())
                .status(fdaRecallDto.status())
                .parsedDate(parsedDate)
                .distLocation(simpleLocation)
                .sourceAgency("FDA")
                .build();
    }

    private static ProductDescription buildProductDescription(FdaRecallDto fdaRecallDto) {
        return ProductDescription.builder()
                .companyName(fdaRecallDto.recallingFirm())
                .productName(fdaRecallDto.productDescription())
                .identifiers(List.of(fdaRecallDto.productDescription(), fdaRecallDto.codeInfo()))
                .build();
    }

    private static FoodRecall fsisRecallMapper(FsisRecallDto fsisRecallDto) {
        var simpleLocation = String.join(", ", fsisRecallDto.states());
        var reason = String.join(", ", fsisRecallDto.recallReason());

        var productDescription = collectProductDescription(fsisRecallDto);
        return FoodRecall.builder()
                .recallNumber(fsisRecallDto.recallNumber())
                .recallingFirm(fsisRecallDto.mediaContact())
                .productDescription(productDescription)
                .reasonForRecall(reason)
                .classification(fsisRecallDto.riskLevel())
                .status(fsisRecallDto.recallType())
                .parsedDate(LocalDate.parse(fsisRecallDto.recallDate()))
                .distLocation(simpleLocation)
                .sourceAgency("USDA")
                .infoUrl(fsisRecallDto.recallUrl())
                .build();
    }

    private static ProductDescription collectProductDescription(FsisRecallDto fsisRecallDto) {
        //"Rosina Food Products, Inc. Recalls Ready-To-Eat Frozen Meatball Products Due To Possible Foreign Matter Contamination",
        //"FSIS Issues Public Health Alert for Beef and Pork Products  Due to Misbranding and Undeclared Allergen",
        String companyName = null;
        if (fsisRecallDto.establishment() != null && !fsisRecallDto.establishment().isEmpty()) {
            companyName = String.join(", ", fsisRecallDto.establishment());
        }

        if (fsisRecallDto.title() != null && !fsisRecallDto.title().isEmpty()) {
            String title = fsisRecallDto.title().toUpperCase();
            if (title.contains("RECALLS")) {
                companyName = StringUtils.substringBefore(title, "RECALLS");
            }
        }

        return ProductDescription.builder()
                .companyName(companyName)
                .productName(String.join(", ", fsisRecallDto.productItems()))
                .identifiers(fsisRecallDto.productItems())
                .build();
    }
}
