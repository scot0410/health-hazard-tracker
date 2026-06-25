package com.outbreak_tracker.infrastructure.adapters.outbound.recall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FdaClientConfig {
    @Value("${fda.api.key}")
    private String apiKey;

    @Bean
    public RestClient fdaClient() {
        return RestClient.builder()
                .baseUrl("https://api.fda.gov/")
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(apiKey, ""))
                .build();
    }
}
