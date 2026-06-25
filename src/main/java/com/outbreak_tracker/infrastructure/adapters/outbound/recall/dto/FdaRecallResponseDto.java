package com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FdaRecallResponseDto(
        @JsonProperty("meta") Meta meta,
        @JsonProperty("results") List<FdaRecallDto> results) {}
