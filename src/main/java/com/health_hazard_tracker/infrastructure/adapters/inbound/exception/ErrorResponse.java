package com.health_hazard_tracker.infrastructure.adapters.inbound.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        long timestamp,
        int status,
        String error,
        String message
) {}
