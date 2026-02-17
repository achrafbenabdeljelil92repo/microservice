package org.achraf.ws.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "article_families")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFamille;

    @Column(nullable = false, unique = true, length = 100)
    private String codeFamille;

    @Column(nullable = false, length = 255)
    private String familyLabel;

    @OneToMany(mappedBy = "family",cascade = CascadeType.ALL)
    private List<Article> articles;



}
