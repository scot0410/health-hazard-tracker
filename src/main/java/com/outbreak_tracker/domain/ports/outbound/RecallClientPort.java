package com.outbreak_tracker.domain.ports.outbound;

import com.outbreak_tracker.domain.model.FoodRecall;
import com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto.FdaRecallResponseDto;
import org.jspecify.annotations.Nullable;

public interface RecallClientPort {
    @Nullable
    FoodRecall fetchLatestRecalls();
}
