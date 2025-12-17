package com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.LoginRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.TokenResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.User;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories.UserRepository;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthController(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(request.password())) {
                if (user.getRole() == null || user.getRole().getName() == null || user.getRole().getName().isBlank()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                String token = tokenProvider.generateToken(user.getEmail(), List.of(user.getRole().getName()));
                return ResponseEntity.ok(new TokenResponse(token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
