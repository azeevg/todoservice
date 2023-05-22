package com.azeevg.todoservice.service;

import com.azeevg.todoservice.exception.NotFoundException;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boards;


    @Autowired
    private TaskRepository tasks;

    @Autowired
    private CentralisedUserService userService;

    public List<Board> getAllBoards() {
        return boards.findAll();
    }

    public Board createBoard(Board board) {
        return boards.save(board);
    }

    public Board getBoardWithTasks(UUID id) {
        Optional<Board> optionalBoard = boards.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            board.getTasks().stream().parallel()
                    .forEach(task -> task.setUserInfo(userService.getUser(task.getUser())));
            return board;
        }
        throw new NotFoundException();
    }


    public void deleteBoard(UUID id) {
        Optional<Board> optionalBoard = boards.findById(id);
        if (optionalBoard.isPresent()) {
            boards.deleteById(id);
        }
        throw new NotFoundException();
    }

    public Task addTask(UUID boardId, Task task) {
        Optional<Board> optionalBoard = boards.findById(boardId);
        if (optionalBoard.isPresent()) {
            task.setBoard(optionalBoard.get());
            return tasks.save(task);
        }
        throw new NotFoundException();
    }
}
