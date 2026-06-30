package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
public class FsisClientConfig {

    @Bean
    public RestClient fsisClient() {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        String browserUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

        return RestClient.builder()
                .baseUrl("https://www.fsis.usda.gov")
                .requestFactory(new JdkClientHttpRequestFactory(httpClient)) // Inject browser-behaving client
                // Mirror your working browser's exact fingerprint headers
                .defaultHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .defaultHeader("Accept-Language", "en-US,en;q=0.9")
                .defaultHeader("Priority", "u=0, i")
                .defaultHeader("sec-ch-ua", "\"Google Chrome\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"")
                .defaultHeader("sec-ch-ua-mobile", "?0")
                .defaultHeader("sec-ch-ua-platform", "\"macOS\"")
                .defaultHeader("sec-fetch-dest", "document")
                .defaultHeader("sec-fetch-mode", "navigate")
                .defaultHeader("sec-fetch-site", "none")
                .defaultHeader("sec-fetch-user", "?1")
                .defaultHeader("Upgrade-Insecure-Requests", "1")
                .build();
    }
}

