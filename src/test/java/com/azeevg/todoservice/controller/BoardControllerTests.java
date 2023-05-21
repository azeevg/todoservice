package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.*;
import com.azeevg.todoservice.model.Board;
import com.azeevg.todoservice.model.Task;
import com.azeevg.todoservice.repository.BoardRepository;
import com.azeevg.todoservice.repository.TaskRepository;
import com.azeevg.todoservice.service.CentralisedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.azeevg.todoservice.controller.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardRepository boardRepository;

	@MockBean
	private TaskRepository taskRepository;

	@MockBean
	private CentralisedUserService userService;
	@MockBean
	private RestTemplate restTemplate;


	private Board existingBoard1;
	private Board existingBoard2;
	private BoardDto existingBoardOutputDto1;

	private BoardDto existingBoardOutputDto2;

	private BoardDto boardToCreate;
	private Board createdBoard;

	private TaskDto inputTaskDto1;
	private Task task1;
	private Task task2;

	private TaskDto outputTaskDto1;

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
	public void init() {
		existingBoard1 = createBoard(UUID.randomUUID(), "Board 1", "Description 1");
		existingBoard2 = createBoard(UUID.randomUUID(), "Board 2", "Description 2");
		existingBoardOutputDto1 = BoardMapper.toDto(existingBoard1);
		existingBoardOutputDto2 = BoardMapper.toDto(existingBoard2);

		boardToCreate = createBoardDto("Created Board", "Description of the Created Board");

		createdBoard = BoardMapper.fromDto(boardToCreate);
		createdBoard.setId(UUID.randomUUID());

		task1 = createTask(UUID.randomUUID(), "Task 1", "Task Description 1", userId1, existingBoard2);
		task2 = createTask(UUID.randomUUID(), "Task 2", "Task Description 2", userId2, existingBoard2);

		inputTaskDto1 = TaskMapper.toDto(task1);
		inputTaskDto1.setId(null);
		outputTaskDto1 = TaskMapper.toDto(task1);
	}

	@Test
	public void getAllBoards_ok_2Boards() throws Exception {
		given(boardRepository.findAll()).willReturn(Arrays.asList(existingBoard1, existingBoard2));

		mockMvc.perform(get("/boards"))
				.andExpect(status().isOk())
				.andExpect(content().string(asJsonString(Arrays.asList(existingBoardOutputDto1, existingBoardOutputDto2))));
	}

	@Test
	public void getAllBoards_ok_noBoards() throws Exception {
		given(boardRepository.findAll()).willReturn(Collections.emptyList());

		mockMvc.perform(get("/boards"))
				.andExpect(status().isOk())
				.andExpect(content().string("[]"));
	}

	@Test
	public void createBoard_created() throws Exception {
		given(boardRepository.save(any(Board.class))).willReturn(createdBoard);

		mockMvc.perform(post("/boards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(boardToCreate)))
				.andExpect(status().isCreated());
	}


	@Test
	public void createBoard_badRequest_failedValidation() throws Exception {
		mockMvc.perform(post("/boards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(new BoardDto())))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name", is("cannot be null")))
				.andExpect(jsonPath("$.description", is("cannot be null")));
	}

	@Test
	public void getBoard_ok_noTasks() throws Exception {
		given(boardRepository.findById(createdBoard.getId())).willReturn(Optional.of(createdBoard));

		mockMvc.perform(get("/boards/{id}", createdBoard.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string(asJsonString(BoardMapper.toDto(createdBoard))));
	}

	@Test
	public void getBoard_ok_withTasks() throws Exception {
		existingBoard2.setTasks(List.of(task1, task2));

		given(boardRepository.findById(existingBoard2.getId())).willReturn(Optional.of(existingBoard2));
		given(userService.getUser(userId1)).willReturn(userInfo1);
		given(userService.getUser(userId2)).willReturn(userInfo2);

		TaskDto taskDto1 = TaskMapper.toDto(task1);
		taskDto1.setUserInfo(userInfo1);
		TaskDto taskDto2 = TaskMapper.toDto(task2);
		taskDto2.setUserInfo(userInfo2);

		existingBoardOutputDto2.setTasks(List.of(taskDto1, taskDto2));

		mockMvc.perform(get("/boards/{id}", existingBoard2.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string(asJsonString(existingBoardOutputDto2)))
				.andExpect(jsonPath("$.tasks[0].userInfo.name.first").value("Alex"))
				.andExpect(jsonPath("$.tasks[1].userInfo.name.first").value("Patrick"));
	}

	@Test
	public void getBoard_notFound() throws Exception {
		UUID boardId = UUID.randomUUID();

		given(boardRepository.findById(boardId)).willReturn(Optional.empty());

		mockMvc.perform(get("/boards/{id}", boardId))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteBoard_noContent() throws Exception {
		UUID boardId = UUID.randomUUID();

		given(boardRepository.findById(boardId)).willReturn(Optional.of(new Board()));

		mockMvc.perform(delete("/boards/{id}", boardId))
				.andExpect(status().isNoContent());

		verify(boardRepository, times(1)).deleteById(boardId);
	}

	@Test
	public void deleteBoard_notFound() throws Exception {
		UUID boardId = UUID.randomUUID();

		given(boardRepository.findById(boardId)).willReturn(Optional.empty());

		mockMvc.perform(delete("/boards/{id}", boardId))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createTask_created() throws Exception {
		existingBoard2.setTasks(List.of(task1));

		given(boardRepository.findById(existingBoard2.getId())).willReturn(Optional.of(existingBoard2));
		given(taskRepository.save(any(Task.class))).willReturn(task1);

		mockMvc.perform(post("/boards/{id}/tasks", existingBoard2.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(inputTaskDto1)))
				.andExpect(status().isCreated())
				.andExpect(content().string(asJsonString(outputTaskDto1)));
	}

	@Test
	public void createTask_notFound() throws Exception {
		UUID boardId = UUID.randomUUID();

		given(boardRepository.findById(boardId)).willReturn(Optional.empty());

		mockMvc.perform(post("/boards/{id}/tasks", boardId, inputTaskDto1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(inputTaskDto1)))
				.andExpect(status().isNotFound());
	}
}

