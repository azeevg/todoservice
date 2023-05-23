package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.TaskDto;
import com.azeevg.todoservice.dto.TaskMapper;
import com.azeevg.todoservice.exception.NotFoundException;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.azeevg.todoservice.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final UUID userId = UUID.randomUUID();
    private final Board board = createBoard(UUID.randomUUID(), "My board", "My board desc");
    private final Task task = createTask(UUID.randomUUID(), "Task 1", "Task desc 1", userId, board);
    private final Task replacementTask = createTask(UUID.randomUUID(), "Task N", "Task desc", userId, board);
    private TaskDto inputTaskDto;
    private TaskDto outputTaskDto;

    @BeforeEach
    void init() {
        inputTaskDto = TaskMapper.toDto(task);
        inputTaskDto.setId(null);
        outputTaskDto = TaskMapper.toDto(replacementTask);
    }

    @Test
    public void overwriteTask_ok() throws Exception {
        given(taskService.overwriteTask(any(), any(Task.class))).willReturn(replacementTask);

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(outputTaskDto)));
    }

    @Test
    public void overwriteTask_notFound() throws Exception {
        given(taskService.overwriteTask(any(), any())).willThrow(NotFoundException.class);

        mockMvc.perform(put("/tasks/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void overwriteTask_badRequest_failedValidation() throws Exception {
        TaskDto nullableDescriptionTaskDto = createTaskDto(userId.toString(), "Task name", null);
        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nullableDescriptionTaskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ok() throws Exception {
        given(taskService.updateTask(any(), any(Task.class))).willReturn(replacementTask);

        mockMvc.perform(patch("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(outputTaskDto)));
    }


    @Test
    void updateTask_notFound() throws Exception {
        given(taskService.updateTask(any(), any(Task.class))).willThrow(NotFoundException.class);

        mockMvc.perform(patch("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_badRequest_invalidTaskStatus() throws Exception {
        TaskDto invalidStatusTaskDto = createTaskDto(userId.toString(), "Name", "Desc");
        invalidStatusTaskDto.setStatus("INVALID_STATUS");

        mockMvc.perform(patch("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidStatusTaskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_noContent() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_notFound() throws Exception {
        willThrow(NotFoundException.class).given(taskService).deleteTask(any());

        mockMvc.perform(delete("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTaskDto)))
                .andExpect(status().isNotFound());
    }
}