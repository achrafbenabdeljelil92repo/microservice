package org.achraf.ws.stockservice.web;


import org.achraf.ws.stockservice.config.kafka.kafkaConfig.kafkaEvents.StockCreatedEvent;
import org.achraf.ws.stockservice.dtos.StockMovementRequestDto;
import org.achraf.ws.stockservice.dtos.StockMovementResponseDto;
import org.achraf.ws.stockservice.dtos.UserDTO;
import org.achraf.ws.stockservice.entities.Article;
import org.achraf.ws.stockservice.entities.StockMovement;
import org.achraf.ws.stockservice.enums.StockActivity;
import org.achraf.ws.stockservice.feigns.UserFeign;
import org.achraf.ws.stockservice.repository.ArticleRepository;
import org.achraf.ws.stockservice.repository.StockMovementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    private static final Logger logger =
            LoggerFactory.getLogger(StockMovementController.class);
    private final UserFeign userFeign;
    private final StockMovementRepository repository;
    private final ArticleRepository articleRepository;
    KafkaTemplate<String, StockCreatedEvent> kafkaTemplate;
    public StockMovementController(StockMovementRepository repository,
                                   ArticleRepository articleRepository,
                                   UserFeign userFeign,
                                   KafkaTemplate<String, StockCreatedEvent> kafkaTemplate) {
        this.articleRepository = articleRepository;
        this.repository = repository;
        this.userFeign = userFeign;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public List<StockMovement> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDto> getById(@PathVariable Long id) {

        return repository.findById(id)
                .map(movement -> {

                    // Appel Feign pour récupérer les infos user
                    UserDTO user = userFeign.getUserById(movement.getUserId()).getBody();

                    StockMovementResponseDto dto =
                            new StockMovementResponseDto(
                                    movement.getId(),
                                    movement.getType(),
                                    movement.getQuantity(),
                                    movement.getMovementDate(),
                                    movement.getArticle().getIdArticle(),
                                    user
                            );

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockMovement> create(@RequestBody StockMovementRequestDto movement) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setMovementDate(java.time.LocalDateTime.now());
        stockMovement.setUserId(movement.getUserId());
        stockMovement.setQuantity(movement.getQuantity());
        stockMovement.setMovementDate(java.time.LocalDateTime.now());
        Article article = articleRepository.findById(movement.getArticleId()).get();//ajouter handle exception
        stockMovement.setArticle(article);
        StockMovement saved = repository.save(stockMovement);
        // optional: set creation date
        StockCreatedEvent event = new StockCreatedEvent();
        event.setStockActivity(StockActivity.CREATED_STOCK_ACTIVITY);
        event.setDescription("Stock movement created");
        // 🔥 Build dynamic URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()        // http://localhost:8080/api/stock-movements
                .path("/{id}")               // add /{id}
                .buildAndExpand(saved.getId())
                .toUri();
        kafkaTemplate.send("producer-stock-event", event);

        logger.info("StockCreatedEvent sent to Kafka for movementId={}", saved.getId());
        return ResponseEntity.created(location).body(saved);
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
