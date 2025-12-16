package com.m01project.taskmanager.dto;

import com.m01project.taskmanager.domain.Board;

import java.time.LocalDateTime;

public class BoardResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.createdAt = board.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
