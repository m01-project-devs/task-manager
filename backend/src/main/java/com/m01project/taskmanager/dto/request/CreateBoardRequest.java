package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBoardRequest {

    @NotBlank(message = "Board name cannot be empty")
    private String title;

}
