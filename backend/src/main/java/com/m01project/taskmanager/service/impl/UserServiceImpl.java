package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.exception.UserAlreadyExistsException;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User create(UserCreateRequestDto request) {
        String email = request.getEmail();
        boolean exists = userRepository.existsByEmail(email);
        if(exists) throw new UserAlreadyExistsException("User already exists.");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User update(String email, UserUpdateRequestDto request) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) throw new ResourceNotFoundException("User not found.");
        User updated = user.get();
        updated.setPassword(request.getPassword());
        updated.setFirstName(request.getFirstName());
        updated.setLastName(request.getLastName());
        return userRepository.save(updated);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean delete(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) return false;
        userRepository.deleteById(user.get().getId());
        return true;
    }
}
