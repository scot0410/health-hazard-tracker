package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record FdaRecallResponseDto(
        @JsonProperty("meta") Meta meta,
        @JsonProperty("results") List<FdaRecallDto> results) {}
