package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.Role;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.exception.InvalidRoleAssignmentException;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.exception.UserAlreadyExistsException;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(()->new ResourceNotFoundException("User is not found."));
    }

    @Override
    public User create(UserCreateRequestDto request) {
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            throw new InvalidRoleAssignmentException("Admin role can not be assigned.");
        }
        boolean exists = userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail());
        if(exists) throw new UserAlreadyExistsException("User already exists.");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User update(String email, UserUpdateRequestDto request) {
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            throw new InvalidRoleAssignmentException("Admin role can not be assigned.");
        }
        User updated = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found."));
        if(request.getPassword() != null) updated.setPassword(passwordEncoder.encode(request.getPassword()));
        if(request.getFirstName() != null) updated.setFirstName(request.getFirstName());
        if(request.getLastName() != null )updated.setLastName(request.getLastName());
        return userRepository.save(updated);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void delete(String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndDeletedAtIsNull(email);
        if(optionalUser.isEmpty()) {throw new ResourceNotFoundException("user not found.");}
        User user = optionalUser.get();
        if(user.getRole() == Role.ADMIN) {
            throw new InvalidRoleAssignmentException("admin users can not be deleted.");
        }
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
