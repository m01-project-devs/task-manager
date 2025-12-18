package com.m01project.taskmanager.dto;

import com.m01project.taskmanager.domain.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.createdAt = board.getCreatedAt();
    }

}
