package org.achraf.ws.authservice.config.kafka.listener.kafkaEvents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.achraf.ws.authservice.enums.StockActivity;

@Getter
@Setter
@NoArgsConstructor
public class StockCreatedEvent {
    StockActivity stockActivity;
    String description;
}
