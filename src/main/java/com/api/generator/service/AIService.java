package com.api.generator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    public String generateCodeFromPrompt(String className, String prompt) {
        String refinedPrompt = refinePrompt(className, prompt);
        String sanitizedPrompt = sanitizeInput(refinedPrompt);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> payload = Collections.singletonMap("inputs", sanitizedPrompt);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON request", e);
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractGeneratedText(response.getBody());
        }
        return "Error generating code.";
    }

    private String extractGeneratedText(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> responseList = objectMapper.readValue(jsonResponse, List.class);

            if (!responseList.isEmpty() && responseList.get(0).containsKey("generated_text")) {
                return responseList.get(0).get("generated_text").toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error processing response.";
    }

    private String refinePrompt(String className, String prompt) {
        return "Generate a single complete Java class named '" + className + "' based on the following description:\n"
                + prompt + "\n\n"
                + "The class should follow Java best practices and include all necessary imports.\n"
                + "The output must be a complete Java class, formatted as:\n"
                + "```java\n"
                + "public class " + className + " {\n"
                + "   // Class implementation\n"
                + "}\n"
                + "```\n"
                + "Do not include summaries or explanations, only return the raw class definition.";
    }

    public String sanitizeInput(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        StringBuilder sanitized = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 0x00 && c <= 0x1F) {
                sanitized.append(String.format("\\u%04x", (int) c));
            } else {
                sanitized.append(c);
            }
        }
        return sanitized.toString();
    }
}
