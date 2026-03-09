package org.achraf.ws.stockservice.web;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.achraf.ws.stockservice.StockMovementType;
import org.achraf.ws.stockservice.dtos.StockMovementResponseDto;
import org.achraf.ws.stockservice.dtos.UserDTO;
import org.achraf.ws.stockservice.entities.Article;
import org.achraf.ws.stockservice.entities.ArticleFamily;
import org.achraf.ws.stockservice.entities.StockMovement;
import org.achraf.ws.stockservice.feigns.UserFeign;
import org.achraf.ws.stockservice.repository.ArticleFamilyRepository;
import org.achraf.ws.stockservice.repository.ArticleRepository;
import org.achraf.ws.stockservice.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StockMovementRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleFamilyRepository   articleFamilyRepository;
    @MockBean
    private UserFeign userFeign;
    @Autowired
    StockMovementRepository stockMovementRepository;

    @BeforeEach
    void cleanDb() {

        stockMovementRepository.deleteAll();
        articleRepository.deleteAll();
        articleFamilyRepository.deleteAll();

        // Create family
        ArticleFamily family = new ArticleFamily();
        family.setCodeFamille("FAM1");
        family.setFamilyLabel("Electronics");

        family = articleFamilyRepository.save(family);

        // Create article
        Article article = new Article();
        article.setCodeArticle("ART1");
        article.setLabel("Laptop");
        article.setStockQuantity(100);
        article.setMinimumThreshold(10);
        article.setUnitPrice(new BigDecimal("1500"));
        article.setFamily(family);

        article = articleRepository.save(article);

        // Create stock movement
        StockMovement stockMovement = new StockMovement();
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setQuantity(10);
        stockMovement.setType(StockMovementType.IN);
        stockMovement.setUserId(1L);
        stockMovement.setArticle(article);

        stockMovementRepository.save(stockMovement);
        Mockito.when(userFeign.getUserById(1L))
                .thenReturn(ResponseEntity.ok(new UserDTO(1L, "Ashraf","a@gmail.com", Set.of("ADMIN"))));
    }

    @Test
    public void shouldGetStockMovementById() {
        StockMovement movement = stockMovementRepository.findAll().get(0);
        var response = restTemplate.getForEntity("/api/stock-movements/" + movement.getId(),
                StockMovementResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(movement.getId(), response.getBody().getId());
        assertEquals(movement.getQuantity(), response.getBody().getQuantity());
    }

    @Test
    void shouldReturnAStockMovementWhenIdExists() {
        StockMovement movement = stockMovementRepository.findAll().get(0);
      ResponseEntity<String> response = restTemplate.getForEntity("/api/stock-movements/"+
                      movement.getId()
              , String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        Number id = json.read("$.id");
        assertThat(id.longValue()).isEqualTo(movement.getId());
        Number quantity = json.read("$.quantity");
        assertThat(quantity.intValue()).isEqualTo(10);



    }
    /*
    @Test
    void should_create_stock_movement() {

        StockMovementRequestDto dto = new StockMovementRequestDto();
        // Create family
        ArticleFamily family = new ArticleFamily();
        family.setCodeFamille("FAM2");
        family.setFamilyLabel("Electronics");
        Article article = new Article();
        article.setMinimumThreshold(850);
        article.setUnitPrice(new BigDecimal("100"));
        article.setFamily(family);

        article = articleRepository.save(article);
        dto.setArticleId(article.getIdArticle());
        dto.setQuantity(10);
        dto.setUserId(1L);

        ResponseEntity<StockMovement> response = restTemplate
                .postForEntity( "/api/stock-movements", dto, StockMovement.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getQuantity());
    }*/
    //voir kafka test

    // Inner class static pour désactiver la sécurité pendant les tests
}
//10 spring web manque codin game test