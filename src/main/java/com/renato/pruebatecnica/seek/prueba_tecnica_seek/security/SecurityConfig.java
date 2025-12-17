package com.renato.pruebatecnica.seek.prueba_tecnica_seek.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.enums.UserRole;

@Configuration
public class SecurityConfig {

        private final JwtTokenProvider tokenProvider;

        public SecurityConfig(JwtTokenProvider tokenProvider) {
                this.tokenProvider = tokenProvider;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenProvider);

                http.csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/auth/login",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/api-docs/**")
                                                .permitAll()
                                                .requestMatchers("/api/v1/clients/**")
                                                .hasAuthority(UserRole.ROLE_ADMIN.getRoleName())
                                                .anyRequest().denyAll())
                                .httpBasic(Customizer.withDefaults())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
