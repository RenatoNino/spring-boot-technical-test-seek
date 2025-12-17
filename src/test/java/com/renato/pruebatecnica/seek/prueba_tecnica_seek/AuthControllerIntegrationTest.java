package com.renato.pruebatecnica.seek.prueba_tecnica_seek;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.controllers.AuthController;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.LoginRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Role;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.User;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories.UserRepository;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.security.JwtTokenProvider;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        LoginRequest request = new LoginRequest("user@email.com", "passowrd");
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").alias("Admin").build();
        User user = User.builder().id(1L).email(request.email()).password(request.password()).role(role).build();

        given(userRepository.findByEmail(eq(request.email()))).willReturn(Optional.of(user));
        given(jwtTokenProvider.generateToken(eq(request.email()), any())).willReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void login_withInvalidPassword_returnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("user@email.com", "wrong");
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").alias("Admin").build();
        User user = User.builder().id(1L).email(request.email()).password("passowrd").role(role).build();

        given(userRepository.findByEmail(eq(request.email()))).willReturn(Optional.of(user));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withUnknownUser_returnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("unknown@email.com", "passowrd");
        given(userRepository.findByEmail(eq(request.email()))).willReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
