package com.m01project.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBoardRequest {

    @NotBlank(message = "Board name cannot be empty")
    private String name;

}
