package com.subrat.identity.sync.messaging.producer;

import com.subrat.identity.sync.domain.event.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdentityEventPublisher {
    private static final String USER_EVENTS_TOPIC = "identity.user.events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public IdentityEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserEvent event) {
        String key = resolvePartitionKey(event);

        log.info("Publishing eventType={} eventId={} key={}",
                event.getEventType(), event.getEventId(), key);

        kafkaTemplate.send(USER_EVENTS_TOPIC, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish eventId={}", event.getEventId(), ex);
                        throw new RuntimeException("Kafka publish failed", ex);
                    }
                });
    }

    private String resolvePartitionKey(UserEvent event) {
        return event.getUserId(); // ordering per user
    }
}
