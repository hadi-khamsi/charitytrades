package com.charitytrades.service;

import com.charitytrades.dto.globalgiving.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class GlobalGivingService {

    private final RestClient restClient;
    private final String apiKey;
    private final RateLimiter rateLimiter;

    public GlobalGivingService(
            RestClient globalGivingRestClient,
            @Qualifier("globalGivingApiKey") String apiKey,
            RateLimiter rateLimiter) {
        this.restClient = globalGivingRestClient;
        this.apiKey = apiKey;
        this.rateLimiter = rateLimiter;
    }

    public List<GGProject> getFeaturedProjects() {
        rateLimiter.acquire();
        try {
            GGProjectsResponse response = restClient.get()
                    .uri("/api/public/projectservice/featured/projects?api_key={apiKey}", apiKey)
                    .retrieve()
                    .body(GGProjectsResponse.class);
            return response != null ? response.getProjectList() : List.of();
        } catch (RestClientException e) {
            System.err.println("Error fetching featured projects: " + e.getMessage());
            return List.of();
        }
    }

    public List<GGProject> getProjectsByTheme(String themeId) {
        rateLimiter.acquire();
        try {
            GGProjectsResponse response = restClient.get()
                    .uri("/api/public/projectservice/themes/{themeId}/projects/active?api_key={apiKey}",
                            themeId, apiKey)
                    .retrieve()
                    .body(GGProjectsResponse.class);
            return response != null ? response.getProjectList() : List.of();
        } catch (RestClientException e) {
            System.err.println("Error fetching projects by theme: " + e.getMessage());
            return List.of();
        }
    }

    public GGProject getProjectById(Long projectId) {
        rateLimiter.acquire();
        try {
            GGProjectsResponse response = restClient.get()
                    .uri("/api/public/projectservice/projects/{projectId}?api_key={apiKey}",
                            projectId, apiKey)
                    .retrieve()
                    .body(GGProjectsResponse.class);
            List<GGProject> projects = response != null ? response.getProjectList() : List.of();
            return projects.isEmpty() ? null : projects.get(0);
        } catch (RestClientException e) {
            System.err.println("Error fetching project by ID: " + e.getMessage());
            return null;
        }
    }

    public List<GGProject> getProjectsByOrganization(Long organizationId) {
        rateLimiter.acquire();
        try {
            GGProjectsResponse response = restClient.get()
                    .uri("/api/public/projectservice/organizations/{orgId}/projects/active?api_key={apiKey}",
                            organizationId, apiKey)
                    .retrieve()
                    .body(GGProjectsResponse.class);
            return response != null ? response.getProjectList() : List.of();
        } catch (RestClientException e) {
            System.err.println("Error fetching projects by organization: " + e.getMessage());
            return List.of();
        }
    }

    public List<GGTheme> getAvailableThemes() {
        return List.of(
                new GGTheme("edu", "Education"),
                new GGTheme("health", "Health"),
                new GGTheme("climate", "Climate Change"),
                new GGTheme("disaster", "Disaster Relief"),
                new GGTheme("animals", "Animals"),
                new GGTheme("children", "Children"),
                new GGTheme("hunger", "Hunger"),
                new GGTheme("rights", "Human Rights"),
                new GGTheme("gender", "Gender Equality"),
                new GGTheme("water", "Clean Water"),
                new GGTheme("ecdev", "Economic Development"),
                new GGTheme("demgov", "Democracy & Governance"),
                new GGTheme("env", "Environment"),
                new GGTheme("tech", "Technology"),
                new GGTheme("sport", "Sport"),
                new GGTheme("art", "Arts & Culture"),
                new GGTheme("lgbtq", "LGBTQ+"),
                new GGTheme("justice", "Justice & Legal"),
                new GGTheme("refugee", "Refugees & Displaced"),
                new GGTheme("repro", "Reproductive Health"),
                new GGTheme("mentalhealth", "Mental Health"),
                new GGTheme("endabuse", "End Abuse"),
                new GGTheme("covid-19", "COVID-19")
        );
    }
}
