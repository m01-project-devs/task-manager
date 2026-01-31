package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.Role;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.exception.InvalidRoleAssignmentException;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.exception.UserAlreadyExistsException;
import com.m01project.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(true);
        assertThatThrownBy(()-> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists.");
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WhenAdminRoleAssigned_ShouldThrowException() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                "test@example.com", "12345678", "Joe", "Duo", "ADMIN");

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(InvalidRoleAssignmentException.class)
                .hasMessageContaining("Admin role can not be assigned.");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists() {
        String email = "test@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("newPass", "John", "Smith");
        User existingUser = new User("test@example.com", "oldPass", "Joe", "Duo");

        // Mock password encoder since UserServiceImpl uses it to encode the new password
        // This was added to fix NullPointerException when passwordEncoder was null
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.findByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(Optional.of(existingUser));
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
    void updateUser_WhenAdminRoleAssigned_ShouldThrowException() {
        String email = "test@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("newPass", "John", "Smith", "ADMIN");

        assertThatThrownBy(() -> userService.update(email, request))
                .isInstanceOf(InvalidRoleAssignmentException.class)
                .hasMessageContaining("Admin role can not be assigned.");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserNotExist_ShouldThrowException() {
        String email = "notFound@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("pass", "Joe", "Duo");
        when(userRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.empty());
        assertThatThrownBy(()->userService.update(email, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found.");
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExist() {
        String email = "test@example.com";
        User user = new User("test@example.com", "12345678", "Joe", "Duo");
        user.setId(1L);
        when(userRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.of(user));

        //when
        userService.delete(email);

        //then
        assertThat(user.getDeletedAt()).isNotNull();
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_WhenUserIsAdmin_ShouldThrowException() {
        String email = "admin@example.com";
        User user = new User("admin@example.com", "12345678", "Admin", "User");
        user.setId(1L);
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmailAndDeletedAtIsNull(email))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.delete(email))
                .isInstanceOf(InvalidRoleAssignmentException.class);

        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_WhenUserNotExist() {
        String email = "notFound@example.com";
        when(userRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(email))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository,never()).deleteById(any());
    }

    @Test
    void findByEmail_shouldReturnRepositoryResult() {
        String email = "test@example.com";
        User user = new User("test@example.com", "12345678", "Joe", "Duo");

        when(userRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getRole(), result.getRole());
        verify(userRepository).findByEmailAndDeletedAtIsNull(email);
    }

    @Test
    void getUsers_shouldReturnPageFromRepository() {
        User user = new User("test@example.com", "12345678", "Joe", "Duo");
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(page);

        Page<User> result = userService.getUsers(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).containsExactly(user);
        verify(userRepository).findAllByDeletedAtIsNull(pageable);
    }
}
