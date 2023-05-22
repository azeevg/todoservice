package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.TaskDto;
import com.azeevg.todoservice.dto.TaskMapper;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.model.TaskStatus;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import com.azeevg.todoservice.validation.TaskCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private BoardRepository boards;

    @Autowired
    private TaskRepository tasks;

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> overwriteTask(@PathVariable UUID id, @RequestBody @Validated(TaskCreation.class) TaskDto taskDto) {
        Optional<Task> optionalTask = tasks.findById(id);
        if (optionalTask.isPresent()) {
            Board board = optionalTask.get().getBoard();
            tasks.deleteById(id);

            Task task = TaskMapper.fromDto(taskDto);
            task.setBoard(board);
            Task savedTask = tasks.save(task);

            TaskDto savedTaskDto = TaskMapper.toDto(savedTask);
            return ResponseEntity.ok(savedTaskDto);
        }
        return ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        Optional<Task> optionalTask = tasks.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (taskDto.getName() != null) {
                task.setName(taskDto.getName());
            }
            if (taskDto.getDescription() != null) {
                task.setDescription(taskDto.getDescription());
            }
            if (taskDto.getUser() != null) {
                task.setUser(UUID.fromString(taskDto.getUser()));
            }
            if (taskDto.getStatus() != null) {
                task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
            }
            Task updatedTask = tasks.save(task);
            TaskDto updatedTaskDto = TaskMapper.toDto(updatedTask);
            return ResponseEntity.ok(updatedTaskDto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        Optional<Task> optionalTask = tasks.findById(id);

        if (optionalTask.isPresent()) {
            tasks.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
