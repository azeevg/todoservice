package com.azeevg.todoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private String id;
    private Name name;
    private Location location;

    @Data
    @AllArgsConstructor
    public static class Name {
        private String first;
        private String last;
    }

    @Data
    @AllArgsConstructor
    public static class Location {
        private String city;
        private String country;
        private String postcode;
    }
}
