package com.github.matthewmartin117.healthcare_api.services;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PHIRedactionService {
    private static final Logger log = LoggerFactory.getLogger(PHIRedactionService.class);

    private final RestTemplate restTemplate;
    private final String modelUrl;

    public PHIRedactionService(RestTemplate restTemplate,
                               @Value("${phi.redactor.url:http://phi-redactor:8000/redact}") String modelUrl) {
        this.restTemplate = restTemplate;
        this.modelUrl = modelUrl;
    }

    @SuppressWarnings("rawtypes")
    public String redact(String noteContent) {
        if (noteContent == null || noteContent.isBlank()) {
            log.debug("No content to redact (empty or null). Returning original content.");
            return noteContent;
        }

        Map<String, Object> request = Map.of("text", noteContent);

        log.debug("Calling PHI redactor at {} with payload length={}", modelUrl, noteContent.length());

        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.postForEntity(modelUrl, request, Map.class);
        } catch (RestClientException e) {
            // Don't fail the whole request; log and return original content as fallback
            log.warn("PHI redactor call failed ({}). Returning original content as fallback.", e.getMessage());
            log.debug("PHI redactor exception", e);
            return noteContent;
        }

        Map body = Optional.ofNullable(response)
                           .map(ResponseEntity::getBody)
                           .orElse(null);

        if (body == null) {
            log.warn("Empty response body from PHI redactor at {}. Returning original content.", modelUrl);
            return noteContent;
        }

        Object redacted = body.get("redacted_text");
        if (redacted == null) {
            log.warn("PHI redactor response missing 'redacted_text' field: {}. Returning original content.", body);
            return noteContent;
        }

        String redactedText = redacted.toString();
        log.debug("PHI redactor returned redacted text length={}", redactedText.length());
        return redactedText;
    }
}


