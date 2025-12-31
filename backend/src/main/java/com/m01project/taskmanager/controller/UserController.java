package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.dto.response.UserResponseDto;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @Valid @NotBlank String email) {
        return userService.findByEmail(email)
                .map(user -> new UserResponseDto(user.getEmail(), user.getFirstName(), user.getLastName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<User> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateRequestDto request) {
        User savedUser = userService.create(request);
        UserResponseDto response = new UserResponseDto(savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName());
        URI location = URI.create("/api/users/" + savedUser.getEmail());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable @Valid String email,
                                                      @RequestBody @Valid UserUpdateRequestDto request) {
        User updated = userService.update(email, request);
        UserResponseDto response = new UserResponseDto(updated.getEmail(), updated.getFirstName(), updated.getLastName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid String email) {
        boolean deleted = userService.delete(email);
        if(deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
