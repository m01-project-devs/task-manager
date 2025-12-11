package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.*;

public class UserCreateRequestDto {

    @NotBlank(message = "Email can not be null or blank.")
    @Email(message = "Pls provide a valid email.")
    private String email;

    @NotBlank(message = "Password can not be null or blank.")
    @Size(min = 4, max = 20, message = "password should be between 4 to 20.")
    private String password;

    @NotBlank(message = "Name can not be null or blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Family name can not be null or blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String lastName;

}
