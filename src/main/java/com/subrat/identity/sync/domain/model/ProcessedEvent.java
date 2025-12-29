package com.subrat.identity.sync.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "processed_events")
@Getter
public class ProcessedEvent {
    @Id
    @Column(nullable = false, updatable = false)
    private String eventId;

    private String eventType;
    private String userId;
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
