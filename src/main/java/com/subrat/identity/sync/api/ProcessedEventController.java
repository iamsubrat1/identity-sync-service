package com.subrat.identity.sync.api;

import com.subrat.identity.sync.entity.ProcessedEvent;
import com.subrat.identity.sync.repository.ProcessedEventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProcessedEventController {

    private final ProcessedEventRepository repository;

    public ProcessedEventController(ProcessedEventRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/processed-events")
    public List<ProcessedEvent> getAllProcessedEvents() {
        return repository.findAll();
    }
}
