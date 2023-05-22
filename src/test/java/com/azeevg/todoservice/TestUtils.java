package com.azeevg.todoservice;

import com.azeevg.todoservice.dto.BoardDto;
import com.azeevg.todoservice.dto.TaskDto;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class TestUtils {


    public static Board createBoard(UUID uuid, String name, String description) {
        Board board = new Board();
        board.setId(uuid);
        board.setName(name);
        board.setDescription(description);
        return board;
    }


    public static BoardDto createBoardDto(String name, String description) {
        BoardDto board = new BoardDto();
        board.setName(name);
        board.setDescription(description);
        return board;
    }

    public static Task createTask(UUID uuid, String name, String description, UUID userId, Board board) {
        Task task = new Task();
        task.setId(uuid);
        task.setName(name);
        task.setDescription(description);
        task.setUserId(userId);
        task.setBoard(board);

        return task;
    }

    public static Task createTask(String name, String description, UUID userId) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setUserId(userId);

        return task;
    }

    public static TaskDto createTaskDto(String user, String name, String description) {
        TaskDto taskDto = new TaskDto();
        taskDto.setUser(user);
        taskDto.setName(name);
        taskDto.setDescription(description);
        return taskDto;
    }

    public static String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
