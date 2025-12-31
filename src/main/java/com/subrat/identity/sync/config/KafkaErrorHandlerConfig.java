package com.subrat.identity.sync.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import com.subrat.identity.sync.exception.NonRetryableException;
import com.subrat.identity.sync.exception.RetryableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Configuration
public class KafkaErrorHandlerConfig {
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        // DeadLetterPublishingRecoverer with logging
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) -> {
                            log.error("Publishing message to DLQ for topic {} partition {} offset {} due to exception: {}",
                                    record.topic(), record.partition(), record.offset(), ex.getMessage(), ex);
                            return new TopicPartition(record.topic() + ".DLQ", record.partition());
                        }
                );

        // Retry: 3 times with 2s delay
        FixedBackOff backOff = new FixedBackOff(2000L, 3);

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

        // Tell Kafka what to retry
        handler.addRetryableExceptions(RetryableException.class);

        // Tell Kafka what NOT to retry
        handler.addNotRetryableExceptions(NonRetryableException.class);
        // Add retry listener to log each retry attempt
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("Retry attempt {} for message in topic {} partition {} offset {} due to exception: {}",
                    deliveryAttempt,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    ex.getMessage());
        });
        return handler;
    }
}
