package com.azeevg.todoservice.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private String id;
    private Name name;
    private Location location;

    @Data
    public static class Name {
        private String first;
        private String last;
    }

    @Data
    public static class Location {
        private String city;
        private String country;
        private String postcode;
    }
}
