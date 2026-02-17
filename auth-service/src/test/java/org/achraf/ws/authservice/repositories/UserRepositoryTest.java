package org.achraf.ws.authservice.repositories;

import org.achraf.ws.authservice.entities.Role;
import org.achraf.ws.authservice.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Test
    public void add_Roles_user() {

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleUser = roleRepository.save(roleUser);
        roleAdmin = roleRepository.save(roleAdmin);

        User user = new User();
        user.setUsername("achraf");
        user.setEmail("achraf@test.com");
        user.getRoles().add(roleAdmin);
        user.getRoles().add(roleUser);
        user = userRepository.save(user);
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(savedUser.getRoles()).hasSize(2);
        assertThat(savedUser.getRoles()).extracting(Role::getName)
                .containsExactlyInAnyOrder(roleUser.getName(), roleAdmin.getName());
    }
}
