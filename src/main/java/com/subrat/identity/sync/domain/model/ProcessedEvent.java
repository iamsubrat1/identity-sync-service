package com.subrat.identity.sync.domain.model;

import com.subrat.identity.sync.domain.event.EventType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "processed_events")
@Getter
public class ProcessedEvent {
    @Id
    @Column(nullable = false, updatable = false)
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    private String userId;

    @Column(nullable = false)
    private Instant processedAt;

    protected ProcessedEvent() {
    }

    public ProcessedEvent(String eventId, EventType eventType, String userId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.userId = userId;
        this.processedAt = Instant.now();
    }
}
