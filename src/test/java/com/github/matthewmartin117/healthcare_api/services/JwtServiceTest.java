package com.github.matthewmartin117.healthcare_api.services;
// This is a unit test for JWT/token services

import org.junit.jupiter.api.Test;
// unit tests for ecurity are mostly just to verify that tokens are generated and validated correctly
// and that usernames can be extracted from valid tokens
public class JwtServiceTest {
  private JwtService jwtService = new JwtService();

  @Test
  // test that a valid token for "testuser" correctly extracts the username "testuser"
  void validToken_extractsUsername() {
    // generate a token for "testuser", then extract username and verify. it matches
    String token = jwtService.generateToken("testuser");
    String username = jwtService.extractUsername(token);
    assert(username.equals("testuser"));
  }

  @Test
  // test that a token is generated correctly
  void generateToken_createsValidToken() {
    String token = jwtService.generateToken("anotheruser");
    assert(token != null && !token.isEmpty());
  }
  @Test
  // test that a valid token is validated correctly
  void validateToken_validToken_returnsTrue() {
    String token = jwtService.generateToken("validuser");
    boolean isValid = jwtService.validateToken(token);
    assert(isValid);
  }
  @Test
  // test that an invalid token is rejected
  void validateToken_invalidToken_returnsFalse() {
    String invalidToken = "invalid.token.value";
    boolean isValid = jwtService.validateToken(invalidToken);
    assert(!isValid);
  }

}
