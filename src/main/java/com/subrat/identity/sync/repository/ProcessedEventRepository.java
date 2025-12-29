package com.subrat.identity.sync.repository;

import com.subrat.identity.sync.domain.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

    boolean existsById(String eventId);
}
