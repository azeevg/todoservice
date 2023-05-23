package com.azeevg.todoservice.service;

import com.azeevg.todoservice.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CusResponse {
    private List<UserInfoDto> results;
}
