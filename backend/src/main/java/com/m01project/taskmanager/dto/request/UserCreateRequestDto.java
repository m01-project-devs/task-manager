package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.*;

public class UserCreateRequestDto {

    @NotNull(message = "Email can not be null.")
    @Email(message = "Pls provide a valid email.")
    private String email;

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
