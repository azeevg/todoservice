package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;

import java.util.UUID;

public class BoardMapper {
    public static Board fromDto(BoardDto boardDto) {
        Board board = new Board();
        board.setId(UUID.fromString(boardDto.getId()));
        board.setName(boardDto.getName());
        board.setDescription(board.getDescription());
        return board;
    }

    public static BoardDto toDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId().toString());
        boardDto.setName(board.getName());
        boardDto.setDescription(board.getDescription());

        return boardDto;
    }
}
