package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest (@NotBlank String token,
                                   @NotBlank String newPassword)
{}
