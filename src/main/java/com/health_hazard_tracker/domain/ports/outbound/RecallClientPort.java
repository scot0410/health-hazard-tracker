package com.health_hazard_tracker.domain.ports.outbound;

import com.health_hazard_tracker.domain.model.FoodRecall;
import org.jspecify.annotations.Nullable;

public interface RecallClientPort {
    @Nullable
    FoodRecall fetchLatestRecalls();
}
