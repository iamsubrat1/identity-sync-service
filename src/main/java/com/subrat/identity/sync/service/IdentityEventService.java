package com.subrat.identity.sync.service;

import com.subrat.identity.sync.api.dto.UserRequest;
import com.subrat.identity.sync.domain.event.EventType;

public interface IdentityEventService {
    void publishEvent(EventType eventType, String userId, UserRequest request);
}
