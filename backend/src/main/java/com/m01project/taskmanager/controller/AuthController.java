package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.dto.request.ForgotPasswordRequest;
import com.m01project.taskmanager.dto.request.LoginRequestDto;
import com.m01project.taskmanager.dto.request.RegisterRequest;
import com.m01project.taskmanager.dto.request.ResetPasswordRequest;
import com.m01project.taskmanager.dto.response.LoginResponseDto;
import com.m01project.taskmanager.dto.response.MessageResponse;
import com.m01project.taskmanager.dto.response.UserResponse;
import com.m01project.taskmanager.service.AuthService;
import com.m01project.taskmanager.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED) 
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto)
    {
        LoginResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        passwordResetService.forgotPassword(request.email());
        return ResponseEntity.ok(new MessageResponse("Password reset link will be sent in few minutes."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully"));
    }
}

