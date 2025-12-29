package com.subrat.identity.sync.service;

import com.subrat.identity.sync.domain.event.UserEvent;
import com.subrat.identity.sync.domain.model.ProcessedEvent;
import com.subrat.identity.sync.repository.ProcessedEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentityProcessingService {

    private final ProcessedEventRepository repository;

    public IdentityProcessingService(ProcessedEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void process(UserEvent event) {

        // idempotency check
        if (repository.existsById(event.getEventId())) {
            return;
        }

        // ---- business logic (mock for now) ----
        switch (event.getEventType()) {
            case USER_CREATED -> handleCreate(event);
            case USER_UPDATED -> handleUpdate(event);
            case USER_DELETED -> handleDelete(event);
        }

        // mark event as processed
        repository.save(new ProcessedEvent(
                event.getEventId(),
                event.getEventType().name(),
                event.getUserId()
        ));
    }

    private void handleCreate(UserEvent event) {
        // call downstream system later (Okta / DB / etc)
    }

    private void handleUpdate(UserEvent event) {
    }

    private void handleDelete(UserEvent event) {
    }
}
