package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client;

import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FdaHttpClient {
    private final RestClient fdaClient;

    public @Nullable FdaRecallResponseDto fetchOngoingRecallData() {
        var pathogens = "listeria salmonella toxoplasma brucella";

        String searchQuery = String.format(
                "classification:\"Class I\" AND status:%s AND reason_for_recall:(%s)",
                "Ongoing",
                pathogens
        );



        return fdaClient.get()
                .uri("/food/enforcement.json/?search={search}", searchQuery)
                .retrieve()
                .body(FdaRecallResponseDto.class);
    }
}
