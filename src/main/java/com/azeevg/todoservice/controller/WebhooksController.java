package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.UserDeletedPayload;
import com.azeevg.todoservice.service.TaskAsyncDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/webhooks")
public class WebhooksController {

    @Autowired
    TaskAsyncDeletionService taskAsyncDeletionService;

    @PostMapping("/user-deleted")
    public ResponseEntity<Void> handleUserDeletedWebhook(@RequestBody UserDeletedPayload payload) {
        UUID userId = payload.getData().getUser();
        taskAsyncDeletionService.deleteTasks(userId);
        return ResponseEntity.ok().build();
    }
}
