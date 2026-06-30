package com.health_hazard_tracker.domain.ports.outbound;

import com.health_hazard_tracker.domain.model.FoodRecall;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RecallClientPort {
    @Nullable
    List<FoodRecall> fetchLatestRecalls();
}
