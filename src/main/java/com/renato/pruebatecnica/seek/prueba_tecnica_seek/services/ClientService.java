package com.renato.pruebatecnica.seek.prueba_tecnica_seek.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.adapters.ClientResponseAdapter;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories.ClientRepository;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.exceptions.BusinessException;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.validations.ClientValidation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientResponseAdapter clientResponseAdapter;
    private final ClientValidation clientValidation;

    public ClientService(ClientRepository clientRepository, ClientResponseAdapter clientResponseAdapter,
            ClientValidation clientValidation) {
        this.clientRepository = clientRepository;
        this.clientResponseAdapter = clientResponseAdapter;
        this.clientValidation = clientValidation;
    }

    @Transactional
    public ClientListResponse saveClient(ClientCreateRequest request) {
        clientValidation.validateCreateClientBody(request);
        Client client = Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .birthDate(request.getBirthDate())
                .build();
        Client savedClient = clientRepository.save(client);
        return clientResponseAdapter.toClientListResponse(savedClient);
    }

    @Transactional
    public ClientListResponse updateClient(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Client not found"));
        clientValidation.validateUpdateClientBody(client, request);
        if (request.getName() != null) {
            client.setName(request.getName());
        }
        if (request.getSurname() != null) {
            client.setSurname(request.getSurname());
        }
        if (request.getAge() != null) {
            client.setAge(request.getAge());
        }
        if (request.getBirthDate() != null) {
            client.setBirthDate(request.getBirthDate());
        }
        Client updated = clientRepository.save(client);
        return clientResponseAdapter.toClientListResponse(updated);
    }

    @Transactional
    public void deleteClient(Long id) {
        clientRepository.findById(id).ifPresent(clientRepository::delete);
    }

    public double calculateAverageAge() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .mapToInt(Client::getAge)
                .average()
                .orElse(0);
    }

    public double calculateStandardDeviation() {
        List<Client> clients = clientRepository.findAll();
        double average = calculateAverageAge();
        return Math.sqrt(clients.stream()
                .mapToDouble(client -> Math.pow(client.getAge() - average, 2))
                .average()
                .orElse(0));
    }

    public List<ClientListResponse> listClients() {
        return clientRepository.findAll().stream()
                .map(clientResponseAdapter::toClientListResponse)
                .collect(Collectors.toList());
    }

}
