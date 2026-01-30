package com.m01project.taskmanager.service;

import com.m01project.taskmanager.dto.request.LoginRequestDto;
import com.m01project.taskmanager.dto.request.RegisterRequest;
import com.m01project.taskmanager.dto.response.LoginResponseDto;
import com.m01project.taskmanager.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponseDto login(LoginRequestDto request);
}
