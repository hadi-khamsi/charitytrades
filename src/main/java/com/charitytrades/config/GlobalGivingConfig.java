package com.charitytrades.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GlobalGivingConfig {

    @Value("${globalgiving.api.key:}")
    private String apiKey;

    @Value("${globalgiving.api.base-url:https://api.globalgiving.org}")
    private String baseUrl;

    @Bean
    public RestClient globalGivingRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public String globalGivingApiKey() {
        return apiKey;
    }
}
