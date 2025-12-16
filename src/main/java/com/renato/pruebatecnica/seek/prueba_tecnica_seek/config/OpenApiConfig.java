package com.renato.pruebatecnica.seek.prueba_tecnica_seek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clientManagementApi() {
        return new OpenAPI().info(new Info()
                .title("Client Management API")
                .description("Operations for client registration, metrics, and listing")
                .version("v1"));
    }
}
