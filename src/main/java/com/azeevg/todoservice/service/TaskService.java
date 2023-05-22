package com.azeevg.todoservice.service;

import com.azeevg.todoservice.exception.NotFoundException;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private BoardRepository boards;

    @Autowired
    private TaskRepository tasks;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Task overwriteTask(UUID id, Task task) {
        Optional<Task> optionalTask = tasks.findById(id);
        if (optionalTask.isPresent()) {
            tasks.deleteById(id);

            Board board = optionalTask.get().getBoard();
            task.setBoard(board);
            return tasks.save(task);
        }
        throw new NotFoundException();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Task updateTask(Task task) {
        Optional<Task> optionalTask = tasks.findById(task.getId());
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            if (task.getName() != null) {
                existingTask.setName(task.getName());
            }
            if (task.getDescription() != null) {
                existingTask.setDescription(task.getDescription());
            }
            if (task.getUserId() != null) {
                existingTask.setUserId(task.getUserId());
            }
            if (task.getStatus() != null) {
                existingTask.setStatus(task.getStatus());
            }
            return tasks.save(existingTask);
        }
        throw new NotFoundException();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteTask(UUID id) {
        Optional<Task> optionalTask = tasks.findById(id);
        if (optionalTask.isPresent()) {
            tasks.deleteById(id);
            return;
        }
        throw new NotFoundException();
    }
}
