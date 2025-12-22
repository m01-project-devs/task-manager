package com.m01project.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoItemResponse {
    private String title;
    private Long id;
    private String description;
    private boolean completed;
}
