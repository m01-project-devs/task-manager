package com.m01project.taskmanager.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto
{
    @Email     // Ensures valid email format
    @NotBlank   // Cannot be null or empty
    private String email;
    @NotBlank
    private String password;
}
