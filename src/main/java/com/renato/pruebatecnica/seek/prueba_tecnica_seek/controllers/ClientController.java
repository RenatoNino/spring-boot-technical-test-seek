package com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.adapters.ClientResponseAdapter;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.MetricsResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.services.ClientServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientServiceImpl clientService;
    private final ClientResponseAdapter clientResponseAdapter;

    public ClientController(ClientServiceImpl clientService, ClientResponseAdapter clientResponseAdapter) {
        this.clientService = clientService;
        this.clientResponseAdapter = clientResponseAdapter;
    }

    @PostMapping
    public ResponseEntity<ClientListResponse> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientListResponse response = clientResponseAdapter.toClientListResponse(clientService.saveClient(request));
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientListResponse> updateClient(
            @PathVariable Long id,
            @RequestBody ClientUpdateRequest request) {
        ClientListResponse response = clientResponseAdapter
                .toClientListResponse(clientService.updateClient(id, request));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientListResponse>> listClients() {
        List<ClientListResponse> clients = clientService.listClients().stream()
                .map(clientResponseAdapter::toClientListResponse).toList();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> getMetrics() {
        double averageAge = clientService.calculateAverageAge();
        double standardDeviation = clientService.calculateStandardDeviation();
        return ResponseEntity.ok(new MetricsResponse(averageAge, standardDeviation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
