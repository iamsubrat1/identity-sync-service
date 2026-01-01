# Identity Sync Service

Identity Sync Service is an event-driven backend service responsible for
handling user lifecycle events (create, update, delete) in a reliable
and scalable manner using Apache Kafka.

The service follows asynchronous processing principles and is designed
to be resilient to failures using retries, idempotency, and dead-letter queues.

---

## Problem Statement

In distributed systems, user identity changes must be processed reliably
without data loss, duplication, or inconsistent state—especially when
downstream systems may fail or respond slowly.

This service demonstrates how to:
- Decouple APIs from processing logic
- Handle failures safely
- Ensure at-least-once processing with idempotency
- Isolate bad events using DLQ

---

## High-Level Design

1. Clients invoke REST APIs to create, update, or delete users.
2. Each API call produces a **UserEvent** and publishes it to Kafka.
3. Kafka acts as a durable event log and buffers traffic.
4. A Kafka consumer processes events asynchronously.
5. Before processing, the consumer checks whether the event was already handled.
6. Events are processed based on type (create/update/delete).
7. Failures are handled using retries and DLQ routing.

---

## Event Flow

- **Input**: HTTP REST requests
- **Transport**: Kafka topic (`identity.user.events`)
- **Processing**: Kafka consumer with retry and idempotency
- **Failure handling**:
    - Retryable errors → retried with backoff
    - Non-retryable or exhausted retries → sent to DLQ

---

## Reliability Guarantees

### Idempotency
Each event has a unique `eventId`.  
Processed events are recorded in the `processed_events` table to prevent
duplicate processing during retries or restarts.

### Retry Mechanism
- Configurable retry attempts with fixed backoff
- Applied only for retryable exceptions

### Dead Letter Queue (DLQ)
- Failed events are published to `identity.user.events.DLQ`
- Prevents poison messages from blocking the main consumer
- Enables future replay or manual inspection

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Kafka
- Apache Kafka
- H2 Database
- JPA / Hibernate

---

## Current Status

Work in progress 🚧  
Core event publishing, consumption, retries, idempotency, and DLQ handling
are implemented.

---

## Future Scope

- Add unit and integration test coverage
- Implement DLQ reprocessing and replay APIs
- Introduce observability (metrics and tracing)
- Integrate downstream identity providers
- Move to production-grade databases and schemas
