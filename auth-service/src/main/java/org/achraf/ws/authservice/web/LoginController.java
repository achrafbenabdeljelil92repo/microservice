package org.achraf.ws.authservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.achraf.ws.authservice.entities.Activity;
import org.achraf.ws.authservice.entities.User;
import org.achraf.ws.authservice.enums.ActivityType;
import org.achraf.ws.authservice.enums.LoginType;
import org.achraf.ws.authservice.repositories.ActivityAuthRepository;
import org.achraf.ws.authservice.repositories.UserRepository;
import org.achraf.ws.authservice.security.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
public class LoginController {
    ActivityAuthRepository activityAuthRepository;
    UserRepository userRepository;
    private JwtDecoder jwtDecoder;
    JwtAuthConverter jwtAuthConverter;
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    public LoginController(ActivityAuthRepository activityAuthRepository,JwtAuthConverter jwtAuthConverter, UserRepository userRepository,JwtDecoder jwtDecoder) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
        this.activityAuthRepository = activityAuthRepository;
    }


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @GetMapping("/auth")
    public ResponseEntity<?> login(@RequestParam String code) {
        try {
            // 🔹 Préparer le corps de la requête
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("code", code);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret); // obligatoire si client confidential
            body.add("scope", "openid profile email");

            // 🔹 Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 🔹 Créer l'entité HTTP
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            // 🔹 Endpoint token Keycloak
            String tokenEndpoint = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            // 🔹 Appel POST pour récupérer le token
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

            // 🔹 Lire le token JWT depuis la réponse
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (!jsonNode.has("access_token")) {
                //à changer exption et login_failed
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No access token returned by Keycloak");
            }
            String jwtString = jsonNode.get("access_token").asText();

            // 🔹 Valider et décoder le JWT via JwtAuthConverter
            Jwt jwt = jwtAuthConverter.getJwt(jwtString); // ajoute une méthode getJwt(String token)
            String username = jwtAuthConverter.getUsername(jwt);
            String email = jwtAuthConverter.getEmail(jwt);

            // 🔹 Vérifier si l'utilisateur existe
            if (!userRepository.existsByUsername(username)) {
                User user = new User();
                user.setLoginType(LoginType.OPENID);
                user.setUsername(username);
                user.setEmail(email);


                userRepository.save(user);
            }
            saveActivity(ActivityType.LOGIN_SUCCESS,username);


            return ResponseEntity.noContent().build();

        } catch (HttpClientErrorException.BadRequest e) {
            // 🔹 Cas de code invalide / expiré
            //à changer handler exception et aop pour log
            saveActivity(ActivityType.LOGIN_FAILED,"guest");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid authorization code: " + e.getResponseBodyAsString());

        } catch (JsonProcessingException e) {
            saveActivity(ActivityType.LOGIN_FAILED,"guest");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error parsing token response");
        } catch (Exception e) {
            saveActivity(ActivityType.LOGIN_FAILED,"guest");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    public void saveActivity(ActivityType activityType,String username) {
        Activity activity = new Activity();
        activity.setActivityType(activityType);
        activity.setDate(LocalDateTime.now());
        if(!username.equals("guest")) {
            User user = userRepository.findByUsername(username).get();
            activity.setUser(user);
        }



        activityAuthRepository.save(activity);
    }


}
