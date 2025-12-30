package com.subrat.identity.sync.config;

import com.subrat.identity.sync.exception.NonRetryableException;
import com.subrat.identity.sync.exception.RetryableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {
    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {

        // Retry: 3 times with 2s delay
        FixedBackOff backOff = new FixedBackOff(2000L, 3);

        DefaultErrorHandler handler = new DefaultErrorHandler(
                (record, ex) -> {
                    // called after retries exhausted
                    // will add DLQ next
                },
                backOff
        );

        // Tell Kafka what to retry
        handler.addRetryableExceptions(RetryableException.class);

        // Tell Kafka what NOT to retry
        handler.addNotRetryableExceptions(NonRetryableException.class);

        return handler;
    }
}
