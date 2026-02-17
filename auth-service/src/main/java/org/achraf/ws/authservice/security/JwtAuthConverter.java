package org.achraf.ws.authservice.security;

import org.achraf.ws.authservice.error.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtDecoder jwtDecoder;

    @Value("${security.jwt.audience}")
    private String expectedAudience;


    public JwtAuthConverter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {

        // Ici jwt est déjà validé par Spring (signature + expiration)

        String username = getUsername(jwt);
        return new JwtAuthenticationToken(jwt, Collections.emptyList(), username);
    }

    public String getUsername(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username");
    }

    public String getEmail(Jwt jwt) {
        return jwt.getClaimAsString("email");
    }

    // 🔐 Méthode complète de validation manuelle
    public Jwt getJwt(String token) {

        try {

            // 1️⃣ Vérifie signature + expiration
            Jwt jwt = jwtDecoder.decode(token);


            // 3⃣ Double vérification expiration
            if (jwt.getExpiresAt() == null ||
                    jwt.getExpiresAt().isBefore(Instant.now())) {
                throw new InvalidTokenException("Token expired");
            }

            // 43⃣ Vérifier username
            String username = jwt.getClaimAsString("preferred_username");
            if (username == null || username.isBlank()) {
                throw new InvalidTokenException("Username missing");
            }

            return jwt;

        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
