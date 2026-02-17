package org.achraf.ws.stockservice.entities;

import jakarta.persistence.*;
import org.achraf.ws.stockservice.StockMovementType;

import java.time.LocalDateTime;

@Entity
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockMovementType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime movementDate;

    // If you want simple foreign keys (no relationship)
    @Column(nullable = false)
    private Integer articleId;

    @Column(nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}
