package com.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Health endpoints - PUBLIC (CRITICAL!)
                        .requestMatchers("/actuator/").permitAll()
                        .requestMatchers("/actuator/health/").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/").permitAll()

                        // Auth endpoints
                        .requestMatchers("/api/auth/").permitAll()

                        // Error
                        .requestMatchers("/error").permitAll()

                        // All others need authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}


