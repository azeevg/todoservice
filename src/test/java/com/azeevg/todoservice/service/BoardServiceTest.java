package com.azeevg.todoservice.service;

import com.azeevg.todoservice.TestUtils;
import com.azeevg.todoservice.dto.UserInfoDto;
import com.azeevg.todoservice.exception.NotFoundException;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.azeevg.todoservice.TestUtils.createTask;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @MockBean
    private CentralisedUserService userService;

    @Autowired
    private BoardService boardService;

    private UUID existingBoardId1;
    private UUID existingBoardId2;
    private UUID taskId1;
    private UUID taskId2;
    private final UUID userId1 = UUID.randomUUID();
    private final UUID userId2 = UUID.randomUUID();

    private final UserInfoDto userInfo1 = new UserInfoDto(
            new UserInfoDto.Name("Alex", "Browse"),
            new UserInfoDto.Location("Berlin", "Germany", "10717")
    );
    private final UserInfoDto userInfo2 = new UserInfoDto(
            new UserInfoDto.Name("Patrick", "Wise"),
            new UserInfoDto.Location("London", "UK", "123AF")
    );

    @BeforeEach
    public void setUp() {
        boardRepository.deleteAll();
        taskRepository.deleteAll();

        Board board1 = TestUtils.createBoard(null, "Board 1", "Desc 1");
        Board board2 = TestUtils.createBoard(null, "Board 2", "Desc 2");
        board1 = boardRepository.save(board1);
        board2 = boardRepository.save(board2);
        existingBoardId1 = board1.getId();
        existingBoardId2 = board2.getId();


        taskRepository.save(createTask(null, "B1 Task 45", "Some description A", userId1, board1));
        taskRepository.save(createTask(null, "B1 Task 46", "Some description B", userId1, board1));
        taskRepository.save(createTask(null, "B1 Task 47", "Some description C", userId2, board1));
        Task task1 = taskRepository.save(createTask(null, "B2 Task 61", "Some description D", userId1, board2));
        Task task2 = taskRepository.save(createTask(null, "B2 Task 62", "Some description E", userId2, board2));

        taskId1 = task1.getId();
        taskId2 = task2.getId();

        given(userService.getUser(userId1)).willReturn(userInfo1);
        given(userService.getUser(userId2)).willReturn(userInfo2);
    }

    @Test
    public void getAllBoards_returns2Boards() throws Exception {
        List<Board> boards = boardService.getAllBoards();
        Assertions.assertEquals(2, boards.size());
        Assertions.assertEquals("Board 1", boards.get(0).getName());
        Assertions.assertEquals("Board 2", boards.get(1).getName());

        Assertions.assertNull(boards.get(1).getTasks().get(0).getUserInfo(), "UserInfo should not be added to the output of the getAllBoards");
    }

    @Test
    public void createBoard_returnsCreatedBoard() throws Exception {
        Board board = TestUtils.createBoard(null, "Board 101", "Created in a test");
        Board newBoard = boardService.createBoard(board);

        Assertions.assertEquals(board, newBoard);
    }

    @Test
    public void getBoardWithTasks_returnsBoardWithTasks() throws Exception {
        Board board = boardService.getBoardWithTasks(existingBoardId2);

        Assertions.assertEquals(2, board.getTasks().size());
        Assertions.assertEquals("B2 Task 61", board.getTasks().get(0).getName());
        Assertions.assertEquals(userInfo1, board.getTasks().get(0).getUserInfo());

        Assertions.assertEquals("B2 Task 62", board.getTasks().get(1).getName());
        Assertions.assertEquals(userInfo2, board.getTasks().get(1).getUserInfo());
    }

    @Test
    public void getBoardWithTasks_throws_NotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> boardService.getBoardWithTasks(UUID.randomUUID()));
    }

    @Test
    public void deleteBoard_deletesBoard() throws Exception {
        boardService.deleteBoard(existingBoardId2);
        Assertions.assertTrue(boardRepository.findById(existingBoardId2).isEmpty(), "Board should have been deleted");
        Assertions.assertTrue(taskRepository.findById(taskId1).isEmpty(), "Task should have been deleted");
        Assertions.assertTrue(taskRepository.findById(taskId2).isEmpty(), "Task should have been deleted");
    }

    @Test
    public void deleteBoard_throws_NotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> boardService.deleteBoard(UUID.randomUUID()));
    }

    @Test
    public void addTask_returnsAddedTask() throws Exception {
        Task task = new Task();
        task.setName("My insane task");
        task.setDescription("Modest description");
        Task addedTask = boardService.addTask(existingBoardId1, task);

        Optional<Task> taskToCheck = taskRepository.findById(addedTask.getId());
        Assertions.assertTrue(taskToCheck.isPresent(), "Task should be available in the repository");
        Assertions.assertEquals("My insane task", taskToCheck.get().getName());

        Optional<Board> board = boardRepository.findById(existingBoardId1);
        Assertions.assertTrue(board.isPresent());
        Assertions.assertEquals(4, board.get().getTasks().size());
    }

    @Test
    public void addTask_throws_NotFoundException() throws Exception {
        Task task = new Task();
        task.setName("My insane task");
        task.setDescription("Modest description");

        Assertions.assertThrows(NotFoundException.class, () -> boardService.addTask(UUID.randomUUID(), task));
    }
}

