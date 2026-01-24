package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest
        (@NotBlank
         @Email
         String email)
{}
