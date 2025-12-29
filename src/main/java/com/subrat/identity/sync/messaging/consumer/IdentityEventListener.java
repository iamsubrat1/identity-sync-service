package com.subrat.identity.sync.messaging.consumer;

import com.subrat.identity.sync.domain.event.UserEvent;
import com.subrat.identity.sync.service.IdentityProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IdentityEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(IdentityEventListener.class);

    private final IdentityProcessingService processingService;

    public IdentityEventListener(IdentityProcessingService processingService) {
        this.processingService = processingService;
    }

    @KafkaListener(
            topics = "identity.user.events",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(UserEvent event) {

        log.info("Consumed eventId={} type={}",
                event.getEventId(), event.getEventType());

        processingService.process(event);
    }
}