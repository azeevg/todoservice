package com.azeevg.todoservice.service;

import com.azeevg.todoservice.TestUtils;
import com.azeevg.todoservice.exception.NotFoundException;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.model.TaskStatus;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    private UUID existingBoardId;
    private final UUID userId = UUID.randomUUID();

    private UUID existingTaskId1;
    private UUID existingTaskId2;

    @BeforeEach
    public void setUp() {
        boardRepository.deleteAll();
        taskRepository.deleteAll();

        Board board = TestUtils.createBoard(null, "Board 301", "Description L");
        Board savedBoard = boardRepository.save(board);
        existingBoardId = savedBoard.getId();

        Task savedTask1 = taskRepository.save(TestUtils.createTask(null, "Task 97", "Board 301 Task 97 Description", userId, savedBoard));
        Task savedTask2 = taskRepository.save(TestUtils.createTask(null, "Task 98", "Board 301 Task 98 Description", userId, savedBoard));

        existingTaskId1 = savedTask1.getId();
        existingTaskId2 = savedTask2.getId();
    }

    @Test
    public void overwriteTask_returnsOverwrittenTask() throws Exception {
        Task task = TestUtils.createTask("Task 999", "This task replaces the existing one", userId);
        Task overwrittenTask = taskService.overwriteTask(existingTaskId1, task);

        Assertions.assertEquals("Task 999", overwrittenTask.getName());

        Optional<Task> replacedTask = taskRepository.findById(existingTaskId1);
        Assertions.assertTrue(replacedTask.isEmpty(), "Old task was replaced");

        Optional<Board> board = boardRepository.findById(existingBoardId);
        List<Task> tasks = board.get().getTasks();

        Assertions.assertEquals(2, tasks.size(), "This board should have exactly 2 tasks");
        Assertions.assertTrue(tasks.contains(overwrittenTask));
    }

    @Test
    public void overwriteTask_throws_NotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> taskService.overwriteTask(UUID.randomUUID(), new Task()));
    }

    @Test
    public void updateTask_returnsUpdatedTask() {
        Task task = TestUtils.createTask("Task 99", "Unique description, have never seen it", null);
        task.setStatus(TaskStatus.STARTED);

        Task updatedTask = taskService.updateTask(existingTaskId1, task);

        Assertions.assertEquals(existingTaskId1, updatedTask.getId());
        Assertions.assertEquals("Task 99", updatedTask.getName());
        Assertions.assertEquals("Unique description, have never seen it", updatedTask.getDescription());
        Assertions.assertEquals(userId, updatedTask.getUserId());
        Assertions.assertEquals(TaskStatus.STARTED, updatedTask.getStatus());

        Optional<Board> board = boardRepository.findById(existingBoardId);

        Assertions.assertEquals(2, board.get().getTasks().size());
        Assertions.assertTrue(board.get().getTasks().contains(updatedTask));
    }

    @Test
    public void deleteTask_deletesTask() {
        taskService.deleteTask(existingTaskId1);

        Optional<Board> board = boardRepository.findById(existingBoardId);
        Assertions.assertTrue(board.isPresent());
        Assertions.assertEquals(1, board.get().getTasks().size());
        Assertions.assertEquals("Task 98", board.get().getTasks().get(0).getName());
    }

    @Test
    public void deleteTask_throws_NotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> taskService.deleteTask(UUID.randomUUID()));
    }
}