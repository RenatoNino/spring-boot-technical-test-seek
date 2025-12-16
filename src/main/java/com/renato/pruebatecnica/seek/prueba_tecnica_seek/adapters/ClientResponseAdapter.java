package com.renato.pruebatecnica.seek.prueba_tecnica_seek.adapters;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;

@Component
public class ClientResponseAdapter {

    public ClientListResponse toClientListResponse(Client client) {
        LocalDate estimatedDeathDate = client.getBirthDate().plusYears(80);
        return new ClientListResponse(
                client.getId(),
                client.getName(),
                client.getSurname(),
                client.getAge(),
                estimatedDeathDate);
    }
}
