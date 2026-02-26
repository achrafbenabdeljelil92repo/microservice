package org.achraf.ws.stockservice.config.kafka.kafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration

public class KafkaConfig {
    @Bean
    NewTopic stockTopic() {
        return TopicBuilder.name("producer-stock-event")
                .partitions(2)
                .replicas(2)
                .configs(Map.of("min.insync.replicas","2"))
                .build();
    }
}
