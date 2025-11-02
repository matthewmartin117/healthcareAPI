package com.github.matthewmartin117.healthcare_api.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PHIRedactionService {

  // simple HTTP client for making REST calls
  private final RestTemplate restTemplate = new RestTemplate();

  // make the URL of the FASTAPI service. currently placeholder
  private final String modelUrl = "http://phi-redactor:8000/redact";

  @SuppressWarnings({ "null", "rawtypes" })
  public String redact(String noteContent) {
    // send the json payload to the FASTAPI service
    Map<String, String> request = Map.of("note_content", noteContent);

    // send the POST request to fastAPI
    ResponseEntity<Map> response = restTemplate.postForEntity(modelUrl, request, Map.class);

    // extract the redacted note content from the response
    return (String) response.getBody().get("redacted_note_content");
  }

}
