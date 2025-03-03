package com.api.generator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class AIService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateCodeFromPrompt(String className, String prompt) {
        String refinedPrompt = refinePrompt(className, prompt);
        String sanitizedPrompt = sanitizeInput(refinedPrompt);

        Map<String, String> payload = Map.of("inputs", sanitizedPrompt);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON request", e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 ? extractGeneratedText(response.body()) : "Error generating code.";
        } catch (Exception e) {
            return "Error connecting to AI API: " + e.getMessage();
        }
    }

    private String extractGeneratedText(String jsonResponse) {
        try {
            List<ResponseData> responseList = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ResponseData.class));

            return responseList.stream()
                    .findFirst()
                    .map(ResponseData::generated_text)
                    .map(this::extractJavaCode)
                    .orElse("Error processing response.");
        } catch (Exception e) {
            return "Error processing response.";
        }
    }

    private String extractJavaCode(String response) {
        int startIndex = response.indexOf("```java");
        if (startIndex != -1) {
            response = response.substring(startIndex + 7);
        }

        int endIndex = response.lastIndexOf("```");
        if (endIndex != -1) {
            response = response.substring(0, endIndex);
        }

        return response.strip();
    }

    private String refinePrompt(String className, String prompt) {
        return STR."""
               Generate only a single complete Java class named '\{className}' based on the following description:
               \{prompt}

               Return only the Java code without any explanations, descriptions, comments, or summaries.
               """;
    }

    private String sanitizeInput(String input) {
        return Optional.ofNullable(input)
                .map(str -> str.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""))
                .orElse("");
    }

    private record ResponseData(String generated_text) {}
}
