package com.m01project.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
