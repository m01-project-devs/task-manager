package com.m01project.taskmanager.dto.request;

import com.m01project.taskmanager.security.PasswordConstraints;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = PasswordConstraints.MIN_LENGTH,
            max = PasswordConstraints.MAX_LENGTH,
            message = PasswordConstraints.ERROR_MESSAGE
    )
    @Pattern(
            regexp = PasswordConstraints.REGEX,
            message =  PasswordConstraints.ERROR_MESSAGE
    )
    private String password;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

}
