package org.achraf.ws.stockservice.web;


import org.achraf.ws.stockservice.entities.StockMovement;
import org.achraf.ws.stockservice.repository.StockMovementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementRepository repository;

    public StockMovementController(StockMovementRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StockMovement> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StockMovement create(@RequestBody StockMovement movement) {
        movement.setMovementDate(java.time.LocalDateTime.now()); // optional: set creation date
        return repository.save(movement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> update(@PathVariable Long id, @RequestBody StockMovement details) {
        return repository.findById(id).map(movement -> {
            movement.setType(details.getType());
            movement.setQuantity(details.getQuantity());
            movement.setArticle(details.getArticle());
            movement.setUserId(details.getUserId());
            return ResponseEntity.ok(repository.save(movement));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id).map(movement -> {
            repository.delete(movement);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
