package com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.security.JwtTokenProvider;

record LoginRequest(@NotBlank String username, @NotBlank String password) {
}

record TokenResponse(String token) {
}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // Mocked login for testing. Accepts username "user" and password "password"
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        if ("user".equals(request.username()) && "password".equals(request.password())) {
            String token = tokenProvider.generateToken(request.username());
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(401).build();
    }
}
