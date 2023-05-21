package com.azeevg.todoservice.service;

import com.azeevg.todoservice.dto.UserInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class CusResponse {
    private List<UserInfoDto> results;
}
