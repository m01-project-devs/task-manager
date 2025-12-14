package com.m01project.taskmanager.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        String error,
        int status) {
}
