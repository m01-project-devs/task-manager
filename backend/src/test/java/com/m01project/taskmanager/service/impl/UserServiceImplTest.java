package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.exception.UserAlreadyExistsException;
import com.m01project.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_WhenUserNotExist() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                "test@example.com", "12345678", "Joe", "Duo");
        User savedUser = new User("test@example.com", "12345678", "Joe", "Duo");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        User created = userService.create(request);
        assertThat(created)
                .isNotNull()
                .extracting("email","firstName", "lastName")
                .containsExactly("test@example.com", "Joe", "Duo");
    }

    @Test
    void createUser_WhenUserAlreadyExists_ShouldThrowException() {
        UserCreateRequestDto request = new UserCreateRequestDto("test@example.com", "12345678", "Joe", "Duo");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThatThrownBy(()-> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists.");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists() {
        String email = "test@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("newPass", "John", "Smith");
        User existingUser = new User("test@example.com", "oldPass", "Joe", "Duo");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        User updatedUser = userService.update(email, request);
        assertThat(updatedUser)
                .isNotNull()
                .extracting("password", "firstName", "lastName")
                .containsExactly("newPass", "John", "Smith");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_WhenUserNotExist_ShouldThrowException() {
        String email = "notFound@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("pass", "Joe", "Duo");
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThatThrownBy(()->userService.update(email, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found.");
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExist() {
        String email = "test@example.com";
        User user = new User(1L, "test@example.com", "12345678", "Joe", "Duo", null, null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        boolean result = userService.delete(email);
        assertThat(result).isTrue();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotExist() {
        String email = "notFound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        boolean result = userService.delete(email);
        assertThat(result).isFalse();
        verify(userRepository,never()).deleteById(any());
    }
}
