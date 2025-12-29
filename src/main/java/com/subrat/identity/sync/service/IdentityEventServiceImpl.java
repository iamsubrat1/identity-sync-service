package com.subrat.identity.sync.service;

import com.subrat.identity.sync.api.dto.UserRequest;
import com.subrat.identity.sync.domain.event.EventType;
import com.subrat.identity.sync.domain.event.UserEvent;
import com.subrat.identity.sync.messaging.producer.IdentityEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class IdentityEventServiceImpl implements IdentityEventService {
    private final IdentityEventPublisher publisher;

    public IdentityEventServiceImpl(IdentityEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishEvent(EventType eventType, String userId, UserRequest request) {

        validate(eventType, userId, request);

        UserEvent event = UserEvent.create(eventType, userId, request);

        publisher.publish(event);
    }

    private void validate(EventType eventType, String userId, UserRequest request) {
        if (eventType == null) {
            throw new IllegalArgumentException("EventType must not be null");
        }

        if (eventType != EventType.USER_CREATED && !StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId is required for update/delete events");
        }
    }
}
