package org.achraf.ws.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticle;

    @Column(nullable = false, unique = true, length = 100)
    private String codeArticle;

    @Column(nullable = false, length = 255)
    private String label;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer minimumThreshold;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private ArticleFamily family;



}

