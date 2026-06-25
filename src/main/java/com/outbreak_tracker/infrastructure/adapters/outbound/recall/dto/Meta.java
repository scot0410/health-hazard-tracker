package com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Meta(
        @JsonProperty("disclaimer") String disclaimer,
        @JsonProperty("license") String license,
        @JsonProperty("last_updated") Date lastUpdated,
        @JsonProperty("results") Results results) {}
