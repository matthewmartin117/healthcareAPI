package com.github.matthewmartin117.healthcare_api.filters;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.github.matthewmartin117.healthcare_api.models.AppUser;
// checks every incoming HTTP request for a valid JWT token
// if token is valid, sets authentication in security context
// ensures only authenticated users can access protected resources
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

  private final JwtService jwtService;
  private final UserRepository userRepo;
  // constructor to initialize the jwtService
  // dependency injection of the JwtService
  public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepo) {
    this.jwtService = jwtService;
    this.userRepo = userRepo;
  }

  @Override
  protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request,
      @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
      throws ServletException,IOException {
    String path = request.getServletPath();
    if (path.equals("/auth/login")) {
        filterChain.doFilter(request, response); // skip JWT check for login
        return;
    }
    // extract JWT token from Authorization header
    final String authHeader = request.getHeader("Authorization");
    // check if header is present and starts with "Bearer "
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    // extract token by removing "Bearer " prefix
    String token = authHeader.substring(7);
    // validate the token using jwtService
    if(jwtService.validateToken(token)) {
      // if valid extract username from token
      String username = jwtService.extractUsername(token);
      // create authentication object
      AppUser user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      // set authentication in security context
      // secuirtycontext stores the authenticated user info for the request
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    // continue the filter chain
    // filter chain: filters execute in order, JWT must run before default auth filter
    filterChain.doFilter(request, response);
  }
}