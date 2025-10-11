package com.github.matthewmartin117.healthcare_api.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.services.JwtService;
import com.github.matthewmartin117.healthcare_api.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    // constructor to initialize the repository
    public AuthController(JwtService jwtService,
            PasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
      }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AppUser user = userRepo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", e.getMessage()));
        }
    }
}