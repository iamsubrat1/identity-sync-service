package com.subrat.identity.sync.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEvent {
    private String eventId;
    private EventType eventType;
    private String userId;
    private Object payload;
    private Instant createdAt;
    private String version;

    public static UserEvent create(EventType type, String userId, Object payload) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(type)
                .userId(userId)
                .payload(payload)
                .createdAt(Instant.now())
                .version("v1")
                .build();
    }
}
