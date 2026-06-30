package com.health_hazard_tracker.domain.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FoodRecall(
        String recallNumber,
        String recallingFirm,
        String productDescription,
        String reasonForRecall,
        String classification,
        String codeInfo,
        String status,
        LocalDate parsedDate,
        String simpleLocation
) {}