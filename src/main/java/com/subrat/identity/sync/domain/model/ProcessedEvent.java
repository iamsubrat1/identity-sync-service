package com.subrat.identity.sync.domain.model;

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
    private String eventType;

    private String userId;

    @Column(nullable = false)
    private Instant processedAt;

    protected ProcessedEvent() {
    }

    public ProcessedEvent(String eventId, String eventType, String userId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.userId = userId;
        this.processedAt = Instant.now();
    }
}
