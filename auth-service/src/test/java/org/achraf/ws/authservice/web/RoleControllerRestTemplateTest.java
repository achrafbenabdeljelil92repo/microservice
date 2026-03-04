package org.achraf.ws.authservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.achraf.ws.authservice.entities.Role;
import org.achraf.ws.authservice.repositories.RoleRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class RoleControllerRestTemplateTest {
   @Autowired
   TestRestTemplate restTemplate;
    @Autowired
    private RoleRepository roleRepository;

    ResponseEntity<Role> roleResponseEntity;
    @BeforeEach
    void cleanDb() {
        roleRepository.deleteAll();
        Role role = new Role();
        role.setName("USER");
        roleResponseEntity = restTemplate.postForEntity("/api/roles",role,Role.class);
    }
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void should_create_role_and_return_201() throws JSONException {
        Role role = new Role();
        role.setName("ADMIN");
        ResponseEntity<Role> response = restTemplate
                .postForEntity("/api/roles",role,Role.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo(role.getName());
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void findAll_should_return_200() {
        Role role = new Role();
        role.setName("ADMIN");
        restTemplate
                .postForEntity("/api/roles", role, Role.class);
        ResponseEntity<Role[]> response = restTemplate.getForEntity("/api/roles",Role[].class);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[1].getName()).isEqualTo(role.getName());
    }
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void findAll_test_json() throws JsonProcessingException {

        ResponseEntity<Role[]> response = restTemplate
                .getForEntity("/api/roles",Role[].class);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response.getBody()); // Convertir en JSON String
        DocumentContext documentContext = JsonPath.parse(json);
        List<String> names = documentContext.read("$..name");
        assertThat((Integer) documentContext.read("$.length()")).isEqualTo(1);


// Assertion avec AssertJ
        assertThat(names).containsExactlyInAnyOrder("USER");

         }
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void delete_role_should_return_204() {
        ResponseEntity<Void> deleteResponse = restTemplate
                .exchange(
                "/api/roles/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                roleResponseEntity.getBody().getId());
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void findById_should_return_200() {
        ResponseEntity<Role> response = restTemplate
                .getForEntity("/api/roles/{id}",
                Role.class, roleResponseEntity.getBody().getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}

