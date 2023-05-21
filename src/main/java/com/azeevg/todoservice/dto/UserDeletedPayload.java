package com.azeevg.todoservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDeletedPayload {
    private String time;
    private UserDeletedData data;

    @Data
    public static class UserDeletedData {
        private UUID user;
    }
}
