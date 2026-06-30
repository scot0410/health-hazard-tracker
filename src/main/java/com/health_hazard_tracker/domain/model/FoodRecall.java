package com.health_hazard_tracker.domain.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FoodRecall(
        String recallNumber,
        String recallingFirm,
        ProductDescription productDescription,
        String reasonForRecall,
        String classification,
        String status,
        LocalDate parsedDate,
        String distLocation,
        String sourceAgency,
        String infoUrl
) {}