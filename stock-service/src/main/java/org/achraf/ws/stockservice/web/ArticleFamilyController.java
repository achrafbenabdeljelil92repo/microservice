package org.achraf.ws.stockservice.web;

import org.achraf.ws.stockservice.entities.ArticleFamily;
import org.achraf.ws.stockservice.repository.ArticleFamilyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
public class ArticleFamilyController {

    private final ArticleFamilyRepository familyRepository;

    public ArticleFamilyController(ArticleFamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    // Get all families
    @GetMapping
    public List<ArticleFamily> getAllFamilies() {
        return familyRepository.findAll();
    }

    // Get family by id
    @GetMapping("/{id}")
    public ResponseEntity<ArticleFamily> getFamilyById(@PathVariable Long id) {
        return familyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a family
    @PostMapping
    public ArticleFamily createFamily(@RequestBody ArticleFamily family) {
        return familyRepository.save(family);
    }

    // Update a family
    @PutMapping("/{id}")
    public ResponseEntity<ArticleFamily> updateFamily(@PathVariable Long id, @RequestBody ArticleFamily familyDetails) {
        return familyRepository.findById(id).map(family -> {
            family.setCodeFamille(familyDetails.getCodeFamille());
            family.setFamilyLabel(familyDetails.getFamilyLabel());
            return ResponseEntity.ok(familyRepository.save(family));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a family
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        return familyRepository.findById(id).map(family -> {
            familyRepository.delete(family);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
