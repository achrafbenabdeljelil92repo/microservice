package org.achraf.ws.authservice.web;

import org.achraf.ws.authservice.entities.Role;
import org.achraf.ws.authservice.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class RoleControllerTest {
   @Autowired
   TestRestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private RoleRepository roleRepository;
    @BeforeEach
    void cleanDb() {
        roleRepository.deleteAll();
    }
    @Test
    void should_create_role_and_return_201() {
        Role role = new Role();
        role.setName("ADMIN");
        ResponseEntity<Role> response = restTemplate.postForEntity("/api/roles",role,Role.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo(role.getName());
    }

   @Test
   void findAll_should_return_200() {
       Role role = new Role();
       role.setName("ADMIN");
       restTemplate.postForEntity("/api/roles",role,Role.class);
       ResponseEntity<List<Role>> response =
    }
}

