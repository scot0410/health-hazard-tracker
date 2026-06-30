package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.client;
import com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto.FsisRecallResponseDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;


@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FsisHttpClient {
    private final RestClient fsisClient;

    public @Nullable List<FsisRecallResponseDto> fetchRecallData() {
        return fsisClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fsis/api/recall/v/1").build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<FsisRecallResponseDto>>() {});
    }
}