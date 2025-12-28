package com.subrat.identity.sync.service.impl;

import com.subrat.identity.sync.api.dto.UserRequest;
import com.subrat.identity.sync.domain.event.EventType;
import com.subrat.identity.sync.domain.event.UserEvent;
import com.subrat.identity.sync.service.IdentityEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class IdentityEventServiceImpl implements IdentityEventService {
    private static final String USER_EVENTS_TOPIC = "identity.user.events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public IdentityEventServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishEvent(EventType eventType, String userId, UserRequest request) {

        validate(eventType, userId, request);

        UserEvent event = UserEvent.create(eventType, userId, request);

        // userId as key guarantees ordering per user
        String key = resolvePartitionKey(event.getUserId());

        log.info("Publishing eventType={} eventId={} key={}",
                event.getEventType(), event.getEventId(), key);

        kafkaTemplate.send(USER_EVENTS_TOPIC, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish eventId={}", event.getEventId(), ex);
                        // In real systems: metrics + retry or outbox
                        throw new RuntimeException("Kafka publish failed", ex);
                    }
                });
    }

    private void validate(EventType eventType, String userId, UserRequest request) {
        if (eventType == null) {
            throw new IllegalArgumentException("EventType must not be null");
        }

        if (eventType != EventType.USER_CREATED && !StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId is required for update/delete events");
        }
    }

    private String resolvePartitionKey(String userId) {
        return userId;
    }
}
