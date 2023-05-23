package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.TaskDto;
import com.azeevg.todoservice.dto.TaskMapper;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.model.TaskStatus;
import com.azeevg.todoservice.service.TaskService;
import com.azeevg.todoservice.validation.TaskCreation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> overwriteTask(@PathVariable UUID id, @RequestBody @Validated(TaskCreation.class) TaskDto taskDto) {
        Task task = TaskMapper.fromDto(taskDto);
        Task overwrittenTask = taskService.overwriteTask(id, task);
        return ResponseEntity.ok(TaskMapper.toDto(overwrittenTask));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskDto taskDto) {
        Task task = new Task();
        if (taskDto.getName() != null) {
            task.setName(taskDto.getName());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getUser() != null) {
            task.setUserId(UUID.fromString(taskDto.getUser()));
        }
        if (taskDto.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
        }
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(TaskMapper.toDto(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
