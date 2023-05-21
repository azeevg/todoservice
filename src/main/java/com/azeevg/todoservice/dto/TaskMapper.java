package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.model.Task;

import java.util.UUID;

public class TaskMapper {
    public static TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId().toString());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setUser(task.getUser().toString());
        taskDto.setStatus(task.getStatus().toString());

        return taskDto;
    }

    public static Task fromDto(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setUser(UUID.fromString(taskDto.getUser()));
        return task;
    }
}
