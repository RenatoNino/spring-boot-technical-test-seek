package com.renato.pruebatecnica.seek.prueba_tecnica_seek.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtTokenProvider tokenProvider;

        public SecurityConfig(JwtTokenProvider tokenProvider) {
                this.tokenProvider = tokenProvider;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain swaggerChain(HttpSecurity http) throws Exception {
                http.securityMatcher(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**")
                                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/auth/**")
                                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }

        @Bean
        @Order(3)
        public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
                JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenProvider);
                http.securityMatcher("/api/**")
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/v1/clients/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .csrf(csrf -> csrf.disable())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
