package com.azeevg.todoservice.controller;

import com.azeevg.todoservice.dto.UserDeletedPayload;
import com.azeevg.todoservice.service.TaskAsyncDeletionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.azeevg.todoservice.TestUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WebhooksController.class)
public class WebhookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskAsyncDeletionService taskAsyncDeletionService;

    @Test
    public void handleUserDeletedWebhook() throws Exception {
        UserDeletedPayload userDeletedPayload = new UserDeletedPayload(
                "time",
                new UserDeletedPayload.UserDeletedData(UUID.randomUUID())
        );


        mockMvc.perform(post("/webhooks/user-deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDeletedPayload)))
                .andExpect(status().isOk());
    }
}
