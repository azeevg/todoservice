package com.azeevg.todoservice.service;

import com.azeevg.todoservice.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class TaskAsyncDeletionServiceTest {
    @Autowired
    private TaskAsyncDeletionService taskAsyncDeletionService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private CacheManager cacheManager;

    @Test
    public void testAsyncTasksDeletion() throws Exception {
        Cache userInfoCache = mock(Cache.class);
        given(cacheManager.getCache(any())).willReturn(userInfoCache);

        UUID uuid = UUID.randomUUID();
        CompletableFuture<Void> deletionFuture = taskAsyncDeletionService.deleteTasks(uuid);
        deletionFuture.join();

        verify(userInfoCache).evict(uuid);
        verify(taskRepository).deleteByUser(uuid);
    }
}
