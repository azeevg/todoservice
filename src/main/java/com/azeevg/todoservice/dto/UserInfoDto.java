package com.azeevg.todoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private Name name;
    private Location location;

    public UserInfoDto() {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Name {
        private String first;
        private String last;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private String city;
        private String country;
        private String postcode;
    }
}
