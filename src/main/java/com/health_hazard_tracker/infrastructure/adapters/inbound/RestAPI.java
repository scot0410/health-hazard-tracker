package com.health_hazard_tracker.infrastructure.adapters.inbound;

import com.health_hazard_tracker.domain.model.FoodRecall;
import com.health_hazard_tracker.domain.service.RecallService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RestAPI {
    private final RecallService recallService;

    @GetMapping("/recalls")
    public ResponseEntity<FoodRecall> getLatestRecalls(){
        return ResponseEntity.ok().body(recallService.fetchLatestRecalls());
    }
}
