package com.outbreak_tracker.infrastructure.adapters.outbound.recall;

import com.outbreak_tracker.domain.model.FoodRecall;
import com.outbreak_tracker.domain.ports.outbound.RecallClientPort;
import com.outbreak_tracker.infrastructure.adapters.outbound.recall.client.FsisHttpClient;
import com.outbreak_tracker.infrastructure.adapters.outbound.recall.client.FdaHttpClient;
import com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto.FsisRecallResponseDto;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
public class RecallAdapter implements RecallClientPort {
    private final FdaHttpClient fdaHttpClient;
    private final FsisHttpClient fsisHttpClient;
    private final ExecutorService virtualExecutor;

    public RecallAdapter(
            FdaHttpClient fdaHttpClient, FsisHttpClient fsisHttpClient,
            @Qualifier("virtualExecutor") ExecutorService virtualExecutor){
        this.fdaHttpClient = fdaHttpClient;
        this.fsisHttpClient = fsisHttpClient;
        this.virtualExecutor = virtualExecutor;
    }

    @Override
    public @Nullable FoodRecall fetchLatestRecalls() {
        CompletableFuture<FdaRecallResponseDto> openFdaRecallFuture = CompletableFuture.supplyAsync(
                fdaHttpClient::fetchRecallData,
                virtualExecutor);

        CompletableFuture<List<FsisRecallResponseDto>> fsisRecallFuture = CompletableFuture.supplyAsync(
                fsisHttpClient::fetchRecallData,
                virtualExecutor);

        return openFdaRecallFuture
                .thenCombine(fsisRecallFuture, RecallAdapter::buildFoodRecall)
                .join();
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
