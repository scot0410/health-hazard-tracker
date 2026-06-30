package com.health_hazard_tracker.domain.service;

import com.health_hazard_tracker.domain.model.FoodRecall;
import com.health_hazard_tracker.domain.ports.outbound.RecallClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecallService {
    private final RecallClientPort clientPort;

    public FoodRecall fetchLatestRecalls() {
        return clientPort.fetchLatestRecalls();
    }
}
