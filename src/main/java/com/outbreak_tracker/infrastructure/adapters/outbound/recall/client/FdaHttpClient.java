package com.outbreak_tracker.infrastructure.adapters.outbound.recall.client;

import com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FdaHttpClient {
    private final RestClient fdaClient;

    public @Nullable FdaRecallResponseDto fetchRecallData() {
        return fdaClient.get()
                .uri("/food/enforcement.json/?search={search}&limit={limit}",
                        "distribution_pattern:\"nationwide\"", 5)
                .retrieve()
                .body(FdaRecallResponseDto.class);
    }
}
