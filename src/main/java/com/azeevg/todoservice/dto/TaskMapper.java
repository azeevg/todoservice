package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.model.Task;

import java.util.Optional;
import java.util.UUID;

public class TaskMapper {
    public static TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(Optional.ofNullable(task.getId()).map(UUID::toString).orElse(null));
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setUser(Optional.ofNullable(task.getUserId()).map(UUID::toString).orElse(null));
        taskDto.setStatus(task.getStatus().toString());
        taskDto.setUserInfo(task.getUserInfo());
        return taskDto;
    }

    public static Task fromDto(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setUserId(Optional.ofNullable(taskDto.getUser()).map(UUID::fromString).orElse(null));
        return task;
    }
}
