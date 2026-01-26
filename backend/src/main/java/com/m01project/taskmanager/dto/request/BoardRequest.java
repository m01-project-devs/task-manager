package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {

    @NotBlank(message = "Board name must not be empty")
    @Size(min = 2, max = 100, message = "Board name must be between 2 and 100 characters")
    private String title;
}
