package com.azeevg.todoservice.service;

import com.azeevg.todoservice.TodoserviceApplication;
import com.azeevg.todoservice.dto.UserInfoDto;
import com.azeevg.todoservice.service.CentralisedUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {TodoserviceApplication.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
public class CentralUserServiceTest {
    @Autowired
    CentralisedUserService userService;


    @Test
    public void testUserInfoCaching() {
        UUID userId = UUID.randomUUID();

        UserInfoDto user1 = userService.getUser(userId);
        assertNotNull(user1);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        userService.setRestTemplate(mockRestTemplate);

        UserInfoDto user2 = userService.getUser(userId);
        assertNotNull(user2);

        verify(mockRestTemplate, never()).getForObject(anyString(), any());
    }
}
