package com.github.matthewmartin117.healthcare_api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class PHIRedactionServiceTest {

  @Mock
  private PHIRedactionService phiRedactionClient;

  @Test
  void testRedactSSN() {
    String input = "Patient SSN: 123-45-6789";
    String expected = "Patient SSN: [REDACTED]";

    when(phiRedactionClient.redact(anyString())).thenReturn(expected);

    String result = phiRedactionClient.redact(input);

    Assertions.assertEquals(expected, result);
  }
}