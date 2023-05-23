package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.BoardDto;
import com.azeevg.todoservice.dto.BoardMapper;
import com.azeevg.todoservice.dto.TaskDto;
import com.azeevg.todoservice.dto.TaskMapper;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.service.BoardService;
import com.azeevg.todoservice.validation.BoardCreation;
import com.azeevg.todoservice.validation.TaskCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        List<BoardDto> boardsDtoList = boardService.getAllBoards().stream().map(BoardMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(boardsDtoList);
    }

    @PostMapping
    public ResponseEntity<BoardDto> createBoard(@RequestBody @Validated(BoardCreation.class) BoardDto boardDto) {
        Board board = BoardMapper.fromDto(boardDto);
        Board createdBoard = boardService.createBoard(board);
        BoardDto createdBoardDto = BoardMapper.toDto(createdBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable UUID id) {
        Board board = boardService.getBoardWithTasks(id);
        BoardDto boardDto = BoardMapper.toDtoWithTasks(board);
        return ResponseEntity.ok(boardDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable UUID id, @RequestBody @Validated(TaskCreation.class) TaskDto taskDto) {
        Task task = TaskMapper.fromDto(taskDto);
        Task createdTask = boardService.addTask(id, task);
        TaskDto createdTaskDto = TaskMapper.toDto(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}