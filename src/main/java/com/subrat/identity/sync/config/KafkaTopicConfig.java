package com.subrat.identity.sync.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("identity.user.events")
                .partitions(3)
                .replicas(1) // local dev; prod = 3
                .build();
    }

    @Bean
    public NewTopic userEventsDlqTopic() {
        return TopicBuilder.name("identity.user.events.DLQ")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
