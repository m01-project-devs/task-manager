package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.LoginRequestDto;
import com.m01project.taskmanager.dto.request.RegisterRequest;
import com.m01project.taskmanager.dto.response.LoginResponseDto;
import com.m01project.taskmanager.dto.response.UserResponse;
import com.m01project.taskmanager.exception.EmailAlreadyUsedException;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.security.JwtService;
import com.m01project.taskmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new EmailAlreadyUsedException("Email is already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User saved = userRepository.save(user);
        return new UserResponse(saved.getEmail());
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponseDto(token);
    }
}
