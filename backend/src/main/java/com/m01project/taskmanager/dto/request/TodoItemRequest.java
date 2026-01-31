package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoItemRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 80, message = "title must be between 1 and 80 characters")
    private String title;

    @Size(max = 255, message = "description must be at most 255 characters")
    private String description;

    private boolean completed;
}
