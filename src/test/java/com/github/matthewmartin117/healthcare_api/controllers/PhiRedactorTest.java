package com.github.matthewmartin117.healthcare_api.controllers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PhiRedactorTest {

  @Mock
  private com.github.matthewmartin117.healthcare_api.services.PHIRedactionService phiRedactionService;

  @Test
  void testRedactText() throws Exception {
    when(phiRedactionService.redact(anyString())).thenReturn("My name is [REDACTED]");
    String result = phiRedactionService.redact("My name is John Doe");
    assertEquals("My name is [REDACTED]", result);
  }

}
