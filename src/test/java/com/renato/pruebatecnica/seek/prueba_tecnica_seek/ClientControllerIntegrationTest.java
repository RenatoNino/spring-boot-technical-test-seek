package com.renato.pruebatecnica.seek.prueba_tecnica_seek;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers.ClientController;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.MetricsResponse;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.exceptions.BusinessException;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.services.ClientService;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @Test
    void createClient_withValidData_returnsCreatedClient() throws Exception {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setAge(30);
        request.setBirthDate(LocalDate.of(1994, 12, 17));

        ClientListResponse created = new ClientListResponse(1L, "John", "Doe", 30, null);
        given(clientService.saveClient(any(ClientCreateRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void createClient_withBusinessError_returnsUnprocessableEntity() throws Exception {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("Jane");
        request.setSurname("Doe");
        request.setAge(20);
        request.setBirthDate(LocalDate.of(2000, 1, 1));

        willThrow(new BusinessException("Age does not match birth date"))
                .given(clientService)
                .saveClient(any(ClientCreateRequest.class));

        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void listClients_returnsClientList() throws Exception {
        ClientListResponse c1 = new ClientListResponse(1L, "A", "B", 30, null);
        ClientListResponse c2 = new ClientListResponse(2L, "C", "D", 25, null);
        given(clientService.listClients()).willReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("A"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].surname").value("D"));
    }

    @Test
    void updateClient_withValidData_returnsUpdatedClient() throws Exception {
        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setSurname("Updated Surname");

        ClientListResponse updated = new ClientListResponse(1L, "Updated Name", "Updated Surname", 30, null);
        given(clientService.updateClient(eq(1L), any(ClientUpdateRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/v1/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.surname").value("Updated Surname"));
    }

    @Test
    void updateClient_withNonExistentId_returnsUnprocessableEntity() throws Exception {
        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Updated Name");

        given(clientService.updateClient(eq(99999L), any(ClientUpdateRequest.class)))
                .willThrow(new BusinessException("Client not found"));

        mockMvc.perform(put("/api/v1/clients/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteClient_withAnyId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getMetrics_returnsMetricsData() throws Exception {
        MetricsResponse metrics = new MetricsResponse(10.0, 2.5);
        given(clientService.calculateAverageAge()).willReturn(metrics.getAverageAge());
        given(clientService.calculateStandardDeviation()).willReturn(metrics.getStandardDeviation());

        mockMvc.perform(get("/api/v1/clients/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(10.0))
                .andExpect(jsonPath("$.standardDeviation").value(2.5));
    }
}
