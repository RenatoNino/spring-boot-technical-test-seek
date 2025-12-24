package com.renato.pruebatecnica.seek.prueba_tecnica_seek.services;

import java.util.List;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;

public interface ClientService {
    public Client saveClient(ClientCreateRequest request);

    public Client updateClient(Long id, ClientUpdateRequest request);

    public void deleteClient(Long id);

    public List<Client> listClients();

    public double calculateAverageAge();

    public double calculateStandardDeviation();
}
