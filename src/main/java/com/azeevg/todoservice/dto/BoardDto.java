package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.validation.BoardCreation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private String id;
    @NotBlank(groups = {BoardCreation.class})
    private String name;
    @NotBlank(groups = {BoardCreation.class})
    private String description;
    private List<TaskDto> tasks;
}
