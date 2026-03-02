package org.achraf.ws.stockservice.web;


import org.achraf.ws.stockservice.entities.Article;

import org.achraf.ws.stockservice.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // Get all articles
    @GetMapping
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // Get article by id
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new article
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article saved = articleRepository.save(article);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getIdArticle()).toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // Update article
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        return articleRepository.findById(id).map(art -> {
            article.setCodeArticle(article.getCodeArticle());
            article.setLabel(article.getLabel());
            article.setStockQuantity(article.getStockQuantity());
            article.setMinimumThreshold(article.getMinimumThreshold());
            article.setUnitPrice(article.getUnitPrice());
            Article updated = articleRepository.save(article);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete article
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        return articleRepository.findById(id).map(article -> {
            articleRepository.delete(article);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

