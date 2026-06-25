package com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductType {
    @JsonProperty("Food")
    FOOD,
    @JsonProperty("Cosmetics")
    COSMETICS,
    @JsonProperty("Biologics")
    BIOLOGICS,
    @JsonProperty("Drugs")
    DRUGS,
    @JsonProperty("Devices")
    DEVICES,
    @JsonProperty("Tobacco")
    TOBACCO
}
