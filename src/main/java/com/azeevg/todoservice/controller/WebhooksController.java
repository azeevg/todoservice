package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.UserDeletedPayload;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class WebhooksController {

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private CacheManager cacheManager;

    public WebhooksController(TaskRepository tasks, CacheManager cacheManager) {
        this.tasks = tasks;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/webhooks/user-deleted")
    public ResponseEntity<Void> handleUserDeletedWebhook(@RequestBody UserDeletedPayload payload) {
        UUID userId = payload.getData().getUser();
        cacheManager.getCache("userInfos").evict(userId);

        List<Task> byUser = tasks.findByUser(userId);
        tasks.deleteAll(byUser);

        return ResponseEntity.ok().build();
    }
}
