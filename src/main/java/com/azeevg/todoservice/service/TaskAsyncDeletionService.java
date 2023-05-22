package com.azeevg.todoservice.service;

import com.azeevg.todoservice.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.azeevg.todoservice.TodoserviceApplication.USER_INFO_CACHE_NAME;

@Service
@Slf4j
public class TaskAsyncDeletionService {
    @Autowired
    private TaskRepository tasks;

    @Autowired
    private CacheManager cacheManager;

    @Async
    public CompletableFuture<Void> deleteTasks(UUID userId) {
        Cache cache = cacheManager.getCache(USER_INFO_CACHE_NAME);
        if (cache != null) {
            cache.evict(userId);
        }
        this.tasks.deleteByUserId(userId);
        return CompletableFuture.completedFuture(null);
    }
}
