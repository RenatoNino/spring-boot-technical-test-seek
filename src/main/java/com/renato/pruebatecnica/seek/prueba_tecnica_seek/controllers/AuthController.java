package com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.LoginRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.TokenResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.enums.UserRole;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        if ("user".equals(request.username()) && "password".equals(request.password())) {
            String token = tokenProvider.generateToken(request.username(), List.of(UserRole.ROLE_ADMIN.getRoleName()));
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(401).build();
    }
}
