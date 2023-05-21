package com.azeevg.todoservice.dto;

import com.azeevg.todoservice.model.TaskStatus;
import com.azeevg.todoservice.validation.TaskCreation;
import com.azeevg.todoservice.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class TaskDto {
    private String id;
    @NotBlank(groups = {TaskCreation.class})
    private String name;
    @NotBlank(groups = {TaskCreation.class})
    private String description;
    @NotBlank(groups = {TaskCreation.class})
    private String user;
    @ValidEnum(enumClass = TaskStatus.class)
    private String status;
    private UserInfoDto userInfo;
}
