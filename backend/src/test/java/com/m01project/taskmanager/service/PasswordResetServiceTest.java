package com.m01project.taskmanager.service;

import com.m01project.emailsender.application.port.EmailMessage;
import com.m01project.emailsender.application.port.EmailSender;
import com.m01project.taskmanager.domain.PasswordResetToken;
import com.m01project.taskmanager.domain.Role;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.exception.InvalidTokenException;
import com.m01project.taskmanager.exception.TokenAlreadyUsedException;
import com.m01project.taskmanager.exception.TokenExpiredException;
import com.m01project.taskmanager.repository.PasswordResetTokenRepository;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.impl.PasswordResetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailSender emailSender;

    @Mock
    private PasswordResetEmailComposer emailComposer;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    private User user;

    @BeforeEach
    void setUp(){
         user = new User();
         user.setId(1L);
         user.setEmail("example@gmail.com");
         user.setPassword("oldEncoded");
         user.setFirstName("Old");
         user.setLastName("Account");
         user.setRole(Role.USER);
    }

    @Test
    void forgotPassword_userExists_savesToken(){
        when(userRepository.findByEmailAndDeletedAtIsNull("example@gmail.com")).thenReturn(Optional.of(user));
        EmailMessage emailMessage = mock(EmailMessage.class);
        when(emailComposer.compose(eq("example@gmail.com"), any(UUID.class))).thenReturn(emailMessage);

        passwordResetService.forgotPassword("example@gmail.com");
        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(passwordResetTokenRepository, times(1)).save(captor.capture());
        PasswordResetToken saved = captor.getValue();

        assertNotNull(saved.getToken());
        assertEquals(user, saved.getUser());
        assertNotNull(saved.getExpiresAt());
        assertNull(saved.getUsedAt());

        verifyNoMoreInteractions(passwordResetTokenRepository);
        verify(emailSender, times(1)).send(emailMessage);
    }

    @Test
    void forgotPassword_userNotExists_doesNotSaveToken() {
        when(userRepository.findByEmailAndDeletedAtIsNull("missing@example.com")).thenReturn(Optional.empty());

        passwordResetService.forgotPassword("missing@example.com");

        verify(passwordResetTokenRepository, never()).save(any());
    }

    @Test
    void resetPassword_invalidUuid_throwsInvalidTokenException() {
        assertThrows(InvalidTokenException.class,
                () -> passwordResetService.resetPassword("not-a-uuid", "NewPass123!"));

        verifyNoInteractions(passwordResetTokenRepository);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }

    @Test
    void resetPassword_tokenNotFound_throwsInvalidTokenException() {
        UUID token = UUID.randomUUID();
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class,
                () -> passwordResetService.resetPassword(token.toString(), "NewPass123!"));

        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
        verify(passwordResetTokenRepository, never()).save(any(PasswordResetToken.class));
    }

    @Test
    void resetPassword_tokenAlreadyUsed_throwsTokenAlreadyUsedException() {
        UUID tokenValue = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .usedAt(LocalDateTime.now().minusMinutes(1))
                .build();

        when(passwordResetTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        assertThrows(TokenAlreadyUsedException.class,
                () -> passwordResetService.resetPassword(tokenValue.toString(), "NewPass123!"));

        verify(passwordResetTokenRepository, times(1)).findByToken(tokenValue);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
        verify(passwordResetTokenRepository, never()).save(any(PasswordResetToken.class));
    }

    @Test
    void resetPassword_tokenExpired_throwsTokenExpiredException() {
        UUID tokenValue = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().minusMinutes(1)) // expired
                .usedAt(null)
                .build();

        when(passwordResetTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        assertThrows(TokenExpiredException.class,
                () -> passwordResetService.resetPassword(tokenValue.toString(), "NewPass123!"));

        verify(passwordResetTokenRepository, times(1)).findByToken(tokenValue);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
        verify(passwordResetTokenRepository, never()).save(any(PasswordResetToken.class));
    }

    @Test
    void resetPassword_validToken_resetsPassword_andMarksTokenUsed() {
        UUID tokenValue = UUID.randomUUID();

        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .usedAt(null)
                .build();

        when(passwordResetTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NewPass123!")).thenReturn("newEncoded");

        passwordResetService.resetPassword(tokenValue.toString(), "NewPass123!");

        assertEquals("newEncoded", user.getPassword());
        assertNotNull(token.getUsedAt());

        verify(passwordResetTokenRepository, times(1)).findByToken(tokenValue);
        verify(passwordEncoder, times(1)).encode("NewPass123!");
        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).save(token);
    }

    @Test
    void resetPassword_tokenUserMissing_throwsInvalidTokenException() {
        UUID tokenValue = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenValue)
                .user(null)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .usedAt(null)
                .build();

        when(passwordResetTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        assertThrows(InvalidTokenException.class,
                () -> passwordResetService.resetPassword(tokenValue.toString(), "NewPass123!"));

        verify(passwordResetTokenRepository, times(1)).findByToken(tokenValue);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
        verify(passwordResetTokenRepository, never()).save(any(PasswordResetToken.class));
    }
}
