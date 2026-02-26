package org.achraf.ws.stockservice.config.kafka.kafkaConfig.kafkaEvents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.achraf.ws.stockservice.enums.StockActivity;

@Getter
@Setter
@NoArgsConstructor
public class StockCreatedEvent {
    StockActivity stockActivity;
    String description;
}
