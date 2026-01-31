package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.dto.response.UserResponseDto;
import com.m01project.taskmanager.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.net.URI;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public Page<User> searchUsers(@RequestParam String query, Pageable pageable) {

        return userService.search(query, pageable);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @Valid @NotBlank String email) {
        User user = userService.findByEmail(email);
        UserResponseDto response = new UserResponseDto(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<User> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/users-only")
    public Page<User> getUsersOnly(Pageable pageable) {
        return userService.getUsersOnly(pageable);
    }

    @GetMapping("/admins-only")
    public Page<User> getAdminsOnly(Pageable pageable) {
        return userService.getAdminsOnly(pageable);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateRequestDto request) {
        User savedUser = userService.create(request);
        UserResponseDto response = new UserResponseDto(savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole().toString());
        URI location = URI.create("/api/users/" + savedUser.getEmail());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable @Valid String email,
                                                      @RequestBody @Valid UserUpdateRequestDto request) {
        User updated = userService.update(email, request);
        UserResponseDto response = new UserResponseDto(updated.getEmail(), updated.getFirstName(), updated.getLastName(), updated.getRole().toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid String email) {
        userService.delete(email);
        return ResponseEntity.ok().build();
    }

}
