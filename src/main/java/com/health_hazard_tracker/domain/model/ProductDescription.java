package com.health_hazard_tracker.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDescription (
        String companyName,
        String productName,
        List<String> identifiers
){}
