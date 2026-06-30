package com.health_hazard_tracker.infrastructure.adapters.outbound.recall;

import com.health_hazard_tracker.domain.model.FoodRecall;
import com.health_hazard_tracker.domain.ports.outbound.RecallClientPort;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client.FsisHttpClient;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client.FdaHttpClient;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FsisRecallResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

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
    public @Nullable FoodRecall fetchLatestRecalls() {

        CompletableFuture<FdaRecallResponseDto> openFdaRecallFuture = CompletableFuture
                .supplyAsync(() -> Optional.ofNullable(fdaHttpClient.fetchRecallData())
                                .orElseGet(() -> FdaRecallResponseDto.builder().build()),
                        virtualExecutor)
                .exceptionally(throwable -> {
                    log.error("Error occurred while fetching OpenFDA data - {}", throwable.getMessage());
                    return FdaRecallResponseDto.builder().build();
                });

        CompletableFuture<List<FsisRecallResponseDto>> fsisRecallFuture = CompletableFuture
                .supplyAsync(() -> Optional.ofNullable(fsisHttpClient.fetchRecallData())
                                .orElseGet(() -> List.of(FsisRecallResponseDto.builder().build())),
                        virtualExecutor)
                .exceptionally(throwable -> {
                    log.error("Error occurred while fetching FSIS data - {}", throwable.getMessage());
                    return List.of(FsisRecallResponseDto.builder().build());
                });

        CompletableFuture.allOf(openFdaRecallFuture, fsisRecallFuture).join();

        var fdaRecallDto = openFdaRecallFuture.join();
        var fsisRecallDto = fsisRecallFuture.join();

        return buildFoodRecall(fdaRecallDto, fsisRecallDto);
    }

    private static FoodRecall buildFoodRecall(FdaRecallResponseDto fdaRecallResponseDto,
                                              List<FsisRecallResponseDto> fsisRecallResponseDto) {
        var results = fdaRecallResponseDto.results().getFirst();
        String reportDate = results.reportDate();
        var parsedDate = reportDate != null
                ? LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
                : null;

        var simpleLocation = String.format("%s, %s, %s", results.city(), results.state(), results.country());

        return FoodRecall.builder()
                .recallNumber(results.recallNumber())
                .recallingFirm(results.recallingFirm())
                .productDescription(results.productDescription())
                .reasonForRecall(results.reasonForRecall())
                .classification(results.classification())
                .codeInfo(results.codeInfo())
                .status(results.status())
                .parsedDate(parsedDate)
                .simpleLocation(simpleLocation)
                .build();
    }
}
