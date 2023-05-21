package com.azeevg.todoservice.repository;

import com.azeevg.todoservice.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID> {
    List<Task> findByUser(UUID user);
}
