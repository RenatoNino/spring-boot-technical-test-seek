package com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.MetricsResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientListResponse> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientListResponse response = clientService.saveClient(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> getMetrics() {
        double averageAge = clientService.calculateAverageAge();
        double standardDeviation = clientService.calculateStandardDeviation();
        return ResponseEntity.ok(new MetricsResponse(averageAge, standardDeviation));
    }

    @GetMapping
    public ResponseEntity<List<ClientListResponse>> listClients() {
        List<ClientListResponse> clients = clientService.listClients();
        return ResponseEntity.ok(clients);
    }
}
