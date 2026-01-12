package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.LoginRequestDto;
import com.m01project.taskmanager.dto.request.RegisterRequest;
import com.m01project.taskmanager.dto.response.LoginResponseDto;
import com.m01project.taskmanager.dto.response.UserResponse;
import com.m01project.taskmanager.exception.EmailAlreadyUsedException;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {


        if (userRepository.existsByEmail(request.getEmail())) {
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

    // Authenticates user and returns JWT token
    public LoginResponseDto login(LoginRequestDto request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));


        // Verify password using BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        //Generate JWT token
        String token = jwtService.generateToken(user);  // ← we used email

        // Return token
        return new LoginResponseDto(token);
    }
}
