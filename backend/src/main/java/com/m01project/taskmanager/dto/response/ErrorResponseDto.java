package com.m01project.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final LocalDateTime timestamp;
    private final String error;
    private final int status;
}
