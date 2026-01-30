package com.m01project.taskmanager.dto.request;

import jakarta.annotation.Nullable;
import com.m01project.taskmanager.security.PasswordConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Nullable
    @Size(
            min = PasswordConstraints.MIN_LENGTH,
            max = PasswordConstraints.MAX_LENGTH,
            message = PasswordConstraints.ERROR_MESSAGE
    )
    @Pattern(
            regexp = PasswordConstraints.REGEX,
            message = PasswordConstraints.ERROR_MESSAGE
    )
    private String password;

    @NotBlank(message = "Name can not be null or blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Name can not be null or blank.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String lastName;

    private String role;
}
