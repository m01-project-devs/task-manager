package com.m01project.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBoardRequest {

    @NotBlank(message = "Board name cannot be empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
