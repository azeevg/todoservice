package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.model.Board;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BoardMapper {
    public static Board fromDto(BoardDto boardDto) {
        Board board = new Board();

        board.setId(Optional.ofNullable(boardDto.getId()).map(UUID::fromString).orElse(null));
        board.setName(boardDto.getName());
        board.setDescription(board.getDescription());
        return board;
    }

    public static BoardDto toDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(Optional.ofNullable(board.getId()).map(UUID::toString).orElse(null));
        boardDto.setName(board.getName());
        boardDto.setDescription(board.getDescription());

        return boardDto;
    }

    public static BoardDto toDtoWithTasks(Board board) {
        BoardDto boardDto = toDto(board);
        boardDto.setTasks(board.getTasks().stream().map(TaskMapper::toDto).collect(Collectors.toList()));
        return boardDto;
    }
}
