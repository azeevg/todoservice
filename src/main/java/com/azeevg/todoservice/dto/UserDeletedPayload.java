package com.azeevg.todoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeletedPayload {
    private String time;
    private UserDeletedData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDeletedData {
        private UUID user;
    }
}
