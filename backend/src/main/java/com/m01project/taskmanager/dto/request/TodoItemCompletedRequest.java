package com.m01project.taskmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoItemCompletedRequest {
    private boolean completed;
}
