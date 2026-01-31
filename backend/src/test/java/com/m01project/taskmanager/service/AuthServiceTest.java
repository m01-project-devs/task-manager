package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.LoginRequestDto;
import com.m01project.taskmanager.dto.request.RegisterRequest;
import com.m01project.taskmanager.dto.response.LoginResponseDto;
import com.m01project.taskmanager.dto.response.UserResponse;
import com.m01project.taskmanager.exception.EmailAlreadyUsedException;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.security.JwtService;
import com.m01project.taskmanager.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_whenEmailUnused_shouldSaveUserAndReturnResponse() {
        RegisterRequest request = new RegisterRequest("test@example.com", "Secret123!", "Joe", "Duo");

        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret123!")).thenReturn("encodedSecret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();

        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getPassword()).isEqualTo("encodedSecret");
        assertThat(saved.getFirstName()).isEqualTo("Joe");
        assertThat(saved.getLastName()).isEqualTo("Duo");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void register_whenEmailUsed_shouldThrowException() {
        RegisterRequest request = new RegisterRequest("test@example.com", "Secret123!", "Joe", "Duo");
        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessage("Email is already in use");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void login_whenCredentialsValid_shouldReturnToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedSecret");

        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("Secret123!");

        when(userRepository.findByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Secret123!", "encodedSecret")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginResponseDto response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_whenUserMissing_shouldThrowBadCredentials() {
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("missing@example.com");
        request.setPassword("Secret123!");

        when(userRepository.findByEmailAndDeletedAtIsNull("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_whenPasswordInvalid_shouldThrowBadCredentials() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedSecret");

        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("WrongPass!");

        when(userRepository.findByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPass!", "encodedSecret")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        verify(jwtService, never()).generateToken(any());
    }
}
