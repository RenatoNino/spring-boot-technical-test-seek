package com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String email, @NotBlank String password) {
}
