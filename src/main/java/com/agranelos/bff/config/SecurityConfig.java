package com.agranelos.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").authenticated() // Require authentication for API endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Allow health checks
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Enable HTTP Basic authentication

        return http.build();
    }
}
