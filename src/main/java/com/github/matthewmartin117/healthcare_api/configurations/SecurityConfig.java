package com.github.matthewmartin117.healthcare_api.configurations;
import com.github.matthewmartin117.healthcare_api.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// configures security settings for the application
@Configuration
public class SecurityConfig {
private final JwtAuthenticationFilter jwtFilter;

public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
}
// security filter chain configuration
// defines how HTTP requests are secured
// adds custom JWT filter to the chain
// ensures JWT filter runs before default authentication filter
// configures which endpoints are public and which require authentication
// currently all endpoints are public (for development purposes)
// in production, sensitive endpoints should require authentication
@SuppressWarnings("removal")
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // disable CSRF for APIs
        // permit all requests to /auth/login, require authentication for other endpoints
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll() // public endpoints
            .anyRequest().authenticated()// all other endpoints are public (change to authenticated() in production)
        )
        // only allow stateless sessions, no session is created or used by Spring Security
        .sessionManagement(session -> session.sessionCreationPolicy(
            org.springframework.security.config.http.SessionCreationPolicy.STATELESS)) // stateless sessions
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // add JWT filter before default auth filter
    return http.build();
}

  /* Password encoder bean uses Bcrpt for hashing
   * critical for secure password storage
   * when authenticating, raw password is hashed and compared to stored hash
   *
   */
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}




/* Authentication procider configuration
 * Links authentication manager to user details service and password encoder
 * Ensures user credentials are verified correctly during login
 * wires springs authentication system
 * later use as a login service to check cresdentials
 */
@Bean
public AuthenticationManager AuthenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}

}
