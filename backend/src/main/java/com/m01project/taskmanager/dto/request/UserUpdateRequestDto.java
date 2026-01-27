package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {

    public UserUpdateRequestDto(String email, String firstName, String lastName) {
        this(email, firstName, lastName, null);
    }

    @Size(min = 4, max = 20, message = "password should be between 4 to 20.")
    private String password;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String firstName;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String lastName;

    private String role;
}
