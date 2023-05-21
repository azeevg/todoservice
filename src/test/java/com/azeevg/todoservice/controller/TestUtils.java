package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.BoardDto;
import com.azeevg.todoservice.dto.UserInfoDto;
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
        task.setUser(userId);
        task.setBoard(board);

        return task;
    }

    public static String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
