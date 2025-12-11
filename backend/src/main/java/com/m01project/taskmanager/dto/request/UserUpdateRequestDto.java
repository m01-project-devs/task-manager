package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateRequestDto {
    @NotNull(message = "Password can not be null.")
    @Size(min = 4, max = 20, message = "password should be between 4 to 20.")
    private String password;

    @NotNull(message = "Name can not be null.")
    @NotBlank(message = "Name can not be blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String firstName;

    @NotNull(message = "Family name can not be null.")
    @NotBlank(message = "Name can not be blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String lastName;
}
