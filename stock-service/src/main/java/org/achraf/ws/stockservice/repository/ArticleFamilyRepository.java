package org.achraf.ws.stockservice.repository;

import org.achraf.ws.stockservice.entities.ArticleFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleFamilyRepository extends JpaRepository<ArticleFamily, Long> {
}
