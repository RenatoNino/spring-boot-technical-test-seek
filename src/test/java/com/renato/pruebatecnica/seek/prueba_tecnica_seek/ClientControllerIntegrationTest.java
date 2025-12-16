package com.renato.pruebatecnica.seek.prueba_tecnica_seek;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientListResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createClient_returnsCreatedClient() {
        String token = obtainToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setAge(30);
        request.setBirthDate(LocalDate.of(1995, 1, 1));

        HttpEntity<ClientCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ClientListResponse> response = restTemplate.exchange(
                url("/api/v1/clients"),
                HttpMethod.POST,
                entity,
                ClientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John");
    }

    private String obtainToken() {
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                url("/auth/login"),
                new LoginRequest("user", "password"),
                TokenResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody().token();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private record LoginRequest(String username, String password) {
    }

    private record TokenResponse(String token) {
    }
}
