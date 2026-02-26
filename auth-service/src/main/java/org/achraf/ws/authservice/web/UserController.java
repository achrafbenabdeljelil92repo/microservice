package org.achraf.ws.authservice.web;

import org.achraf.ws.authservice.dto.UserDTO;
import org.achraf.ws.authservice.entities.Activity;
import org.achraf.ws.authservice.entities.Role;
import org.achraf.ws.authservice.entities.User;
import org.achraf.ws.authservice.error.UserNotFoundException;
import org.achraf.ws.authservice.repositories.ActivityAuthRepository;
import org.achraf.ws.authservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ActivityAuthRepository activityRepository;

    public UserController(UserRepository userRepository, ActivityAuthRepository activityRepository) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {

        // Fetch User entity from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with username: " + username
                ));

        // Map to DTO (only expose safe fields)
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + id
                ));

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(dto);
    }

    // Ajouter une activité à un utilisateur
    @PostMapping("/{userId}/activities")
    public ResponseEntity<?> addActivity(
            @PathVariable Long userId,
            @RequestBody Activity activity
    ) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();

        user.addActivity(activity);  // ajoute à la collection
        userRepository.save(user);   // persiste l'activité grâce à Cascade.PERSIST

        return ResponseEntity.ok(activity);
    }

    // Supprimer une activité d'un utilisateur
    @DeleteMapping("/{userId}/activities/{activityId}")
    public ResponseEntity<?> removeActivity(
            @PathVariable Long userId,
            @PathVariable Long activityId
    ) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Activity> activityOpt = activityRepository.findById(activityId);

        if (userOpt.isEmpty() || activityOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Activity activity = activityOpt.get();

        if (!user.getActivities().contains(activity)) {
            return ResponseEntity.badRequest().body("Activity not associated with user");
        }

        user.removeActivity(activity); // retire du Set, orphanRemoval = true supprime en DB
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // Récupérer toutes les activités d'un utilisateur
    @GetMapping("/{userId}/activities")
    public ResponseEntity<?> getActivities(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        // Si fetch lazy, Hibernate.initialize(user.getActivities()) peut être nécessaire
        return ResponseEntity.ok(user.getActivities());
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

}
