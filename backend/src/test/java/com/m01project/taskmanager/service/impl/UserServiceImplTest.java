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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_WhenUserNotExist() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                "test@example.com", "12345678", "Joe", "Duo");
        User savedUser = new User("test@example.com", "12345678", "Joe", "Duo");
        when(passwordEncoder.encode("12345678")).thenReturn("encoded12345678");
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

        // Create user with 4-argument constructor - Role automatically defaults to USER
        // Previously we had to manually call: existingUser.setRole(Role.USER)
        // Now the constructor handles this automatically
        User existingUser = new User("test@example.com", "oldPass", "Joe", "Duo");

        // Mock password encoder since UserServiceImpl uses it to encode the new password
        // This was added to fix NullPointerException when passwordEncoder was null
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        User updatedUser = userService.update(email, request);

        // Note: We don't assert password here because it gets encoded
        // We only verify firstName and lastName which are set directly
        assertThat(updatedUser)
                .isNotNull()
                .extracting("firstName", "lastName")
                .containsExactly("John", "Smith");
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
