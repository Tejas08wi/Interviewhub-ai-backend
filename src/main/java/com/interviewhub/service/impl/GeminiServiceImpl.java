package com.interviewhub.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.interviewhub.service.GeminiService;

@Service
public class GeminiServiceImpl implements GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String generateResponse(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of(
                                                "text",
                                                prompt)))));

        Map<?, ?> response = webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<?> candidates = (List<?>) response.get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            return "No response from Gemini.";
        }

        Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);

        Map<?, ?> content = (Map<?, ?>) candidate.get("content");

        List<?> parts = (List<?>) content.get("parts");

        Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);

        return firstPart.get("text").toString();
    }

}