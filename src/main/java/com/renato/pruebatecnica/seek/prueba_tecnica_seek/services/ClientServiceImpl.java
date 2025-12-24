package com.renato.pruebatecnica.seek.prueba_tecnica_seek.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories.ClientRepository;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.exceptions.BusinessException;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.validations.ClientValidation;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientValidation clientValidation;
    private final Counter clientCreationCounter;

    public ClientServiceImpl(ClientRepository clientRepository, ClientValidation clientValidation,
            MeterRegistry meterRegistry) {
        this.clientRepository = clientRepository;
        this.clientValidation = clientValidation;
        this.clientCreationCounter = meterRegistry.counter("client.creations.total");
        ;
    }

    @Transactional
    public Client saveClient(ClientCreateRequest request) {
        clientValidation.validateCreateClientBody(request);
        Client client = Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .birthDate(request.getBirthDate())
                .build();
        Client savedClient = clientRepository.save(client);

        clientCreationCounter.increment();

        return savedClient;
    }

    @Transactional
    public Client updateClient(Long id, ClientUpdateRequest request) {
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

        return updated;
    }

    @Transactional
    public void deleteClient(Long id) {
        clientRepository.findById(id).ifPresent(clientRepository::delete);
    }

    public List<Client> listClients() {
        return clientRepository.findAll();
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

}
