package org.achraf.ws.authservice.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.achraf.ws.authservice.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;
    @Test
    void save_and_find_role() {
        // given
        Role role = new Role();
        role.setName("ADMIN");
        Role savedRole = roleRepository.save(role);
        Optional<Role> foundRole = roleRepository.findById(savedRole.getId());

        assertThat(foundRole.isPresent()).isTrue();
        assertThat(foundRole.get().getName()).isEqualTo(savedRole.getName());
    }

    @Test
    void delete_role_by_id() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        Role savedRole = roleRepository.save(role);
        Long roleId = savedRole.getId();

        roleRepository.deleteById(roleId);
        Optional<Role> foundRole = roleRepository.findById(roleId);
        assertThat(foundRole).isEmpty();
    }
}
