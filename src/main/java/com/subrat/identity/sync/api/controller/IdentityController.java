package com.subrat.identity.sync.api.controller;

import com.subrat.identity.sync.api.dto.UserRequest;
import com.subrat.identity.sync.domain.event.EventType;
import com.subrat.identity.sync.service.IdentityEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class IdentityController {

    private final IdentityEventService identityEventService;

    public IdentityController(IdentityEventService identityEventService) {
        this.identityEventService = identityEventService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createUser(@RequestBody UserRequest request) {
        String userId = UUID.randomUUID().toString();
        identityEventService.publishEvent(EventType.USER_CREATED, userId, request);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable String userId,
                           @RequestBody UserRequest request) {
        identityEventService.publishEvent(EventType.USER_UPDATED, userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable String userId) {
        identityEventService.publishEvent(EventType.USER_DELETED, userId, null);
    }
}
