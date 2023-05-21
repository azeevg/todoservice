package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.*;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import com.azeevg.todoservice.service.CentralisedUserService;
import com.azeevg.todoservice.validation.BoardCreation;
import com.azeevg.todoservice.validation.TaskCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
@Validated
public class BoardController {
    @Autowired
    private BoardRepository boards;

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private CentralisedUserService userService;

    public BoardController(BoardRepository boards, TaskRepository tasks, CentralisedUserService userService) {
        this.boards = boards;
        this.tasks = tasks;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        List<BoardDto> boardsDtoList = boards.findAll().stream().map(BoardMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(boardsDtoList);
    }

    @PostMapping
    public ResponseEntity<BoardDto> createBoard(@RequestBody @Validated(BoardCreation.class) BoardDto boardDto) {
        Board board = BoardMapper.fromDto(boardDto);
        Board createdBoard = boards.save(board);
        BoardDto createdBoardDto = BoardMapper.toDto(createdBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable UUID id) {
        Optional<Board> optionalBoard = boards.findById(id);
        if (optionalBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Board board = optionalBoard.get();
            BoardDto boardDto = BoardMapper.toDto(board);
            List<TaskDto> tasks = board.getTasks().stream().parallel().map(this::toDtoWithUserInfo).collect(Collectors.toList());
            boardDto.setTasks(tasks);
            return ResponseEntity.ok(boardDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID id) {
        Optional<Board> optionalBoard = boards.findById(id);
        if (optionalBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        boards.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable UUID id, @RequestBody @Validated(TaskCreation.class) TaskDto taskDto) {
        Optional<Board> optionalBoard = boards.findById(id);
        if (optionalBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Task task = TaskMapper.fromDto(taskDto);
            task.setBoard(optionalBoard.get());
            Task createdTask = tasks.save(task);
            TaskDto createdTaskDto = TaskMapper.toDto(createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
        }
    }


    private TaskDto toDtoWithUserInfo(Task task) {
        TaskDto taskDto = TaskMapper.toDto(task);
        UserInfoDto user = userService.getUser(task.getUser());
        taskDto.setUserInfo(user);
        return taskDto;
    }
}