package com.azeevg.todoservice.repository;

import com.azeevg.todoservice.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends CrudRepository<Task, UUID> {
    List<Task> findByUser(UUID user);
}
