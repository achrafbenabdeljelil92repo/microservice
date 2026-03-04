package org.achraf.ws.authservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // pas de CSRF pour les tests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // toutes les requêtes sont permises
                )
                .httpBasic(Customizer.withDefaults()); // permet d'utiliser TestRestTemplate.withBasicAuth

        return http.build();
    }
}
