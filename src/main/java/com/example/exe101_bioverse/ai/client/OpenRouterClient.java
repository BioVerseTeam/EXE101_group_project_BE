package com.example.exe101_bioverse.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenRouterClient {

    private final RestClient restClient;

    @Value("${bioverse.ai.model}")
    private String aiModel;

    @Value("${bioverse.ai.temperature}")
    private double temperature;

    @Value("${bioverse.ai.max-tokens}")
    private int maxTokens;

    public OpenRouterClient(@Value("${openrouter.base-url}") String baseUrl,
                            @Value("${openrouter.api-key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    /**
     * Sends messages list to OpenRouter completions API and retrieves the generated content.
     */
    @SuppressWarnings("unchecked")
    public String ask(List<Map<String, String>> messages) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiModel);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("messages", messages);

        Map<String, Object> response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, clientResponse) -> {
                    String errorBody;
                    try {
                        errorBody = new String(clientResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        errorBody = "Unable to read error body";
                    }
                    throw new RuntimeException(
                            "OpenRouter API call failed with HTTP status " + clientResponse.getStatusCode()
                                    + ". Response details: " + errorBody
                    );
                })
                .body(Map.class);

        if (response == null) {
            throw new RuntimeException("OpenRouter returned an empty or null response.");
        }

        try {
            List<?> choices = (List<?>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("No choices found in OpenRouter API response: " + response);
            }
            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");
            if (message == null) {
                throw new RuntimeException("No 'message' object found in choice details: " + choice);
            }
            String content = (String) message.get("content");
            if (content == null) {
                throw new RuntimeException("Generated content is null in message object: " + message);
            }
            return content;
        } catch (ClassCastException | NullPointerException e) {
            throw new RuntimeException("Unable to parse OpenRouter response format. Response: " + response, e);
        }
    }
}
