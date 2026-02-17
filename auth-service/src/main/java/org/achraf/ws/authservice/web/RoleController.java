package org.achraf.ws.authservice.web;

import org.achraf.ws.authservice.entities.Role;
import org.achraf.ws.authservice.error.ResourceNotFoundException;
import org.achraf.ws.authservice.repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) {
        Role saved = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Role>> findAll() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id " + id)
                );
        return ResponseEntity.ok(role);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(
            @PathVariable Long id,
            @RequestBody Role updatedRole) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id " + id)
                );

        // Exemple : adapter selon ton entité
        role.setName(updatedRole.getName());

        Role saved = roleRepository.save(role);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id " + id);
        }

        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
