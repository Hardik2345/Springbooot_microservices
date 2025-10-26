package com.cu.collage.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            // Disable framework Basic auth; we handle credential verification in the controller
            .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
