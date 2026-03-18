package org.achraf.ws.authservice.config.kafka.listener;

import org.achraf.ws.authservice.config.kafka.listener.exception.NotRetryableException;
import org.achraf.ws.authservice.config.kafka.listener.exception.RetryableException;
import org.achraf.ws.authservice.config.kafka.listener.kafkaEvents.StockCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "producer-stock-event")
public class StockEventHandler {

    private RestTemplate restTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public StockEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(StockCreatedEvent stockCreatedEvent) {
        LOGGER.info("Received a new event: " + stockCreatedEvent.getStockActivity() + " with productId: "
                + stockCreatedEvent.getDescription());

        try {

            //ajout dans log auth
        } catch (ResourceAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw new RetryableException(ex);
        } catch(HttpServerErrorException ex) {
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

    }

}
