package com.m01project.taskmanager.service.impl;

import com.m01project.emailsender.application.port.EmailSender;
import com.m01project.taskmanager.domain.PasswordResetToken;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.exception.InvalidTokenException;
import com.m01project.taskmanager.exception.TokenAlreadyUsedException;
import com.m01project.taskmanager.exception.TokenExpiredException;
import com.m01project.taskmanager.repository.PasswordResetTokenRepository;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.PasswordResetEmailComposer;
import com.m01project.taskmanager.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int TOKEN_EXPIRES_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;
    private final PasswordResetEmailComposer emailComposer;

    @Override
    public void forgotPassword(String email) {
        userRepository.findByEmailAndDeletedAtIsNull(email).ifPresent(user -> {
            PasswordResetToken token = PasswordResetToken.builder()
                    .user(user)
                    .token(UUID.randomUUID())
                    .expiresAt(LocalDateTime.now().plusMinutes(TOKEN_EXPIRES_MINUTES))
                    .usedAt(null)
                    .build();
            passwordResetTokenRepository.save(token);

            emailSender.send(emailComposer.compose(user.getEmail(), token.getToken()));
        });
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        final UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(token);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException();
        }

        PasswordResetToken prt = passwordResetTokenRepository.findByToken(tokenUuid)
                .orElseThrow(InvalidTokenException::new);

        LocalDateTime now = LocalDateTime.now();

        if (prt.isUsed()) {
            throw new TokenAlreadyUsedException();
        }

        if (prt.isExpired(now)) {
            throw new TokenExpiredException();
        }

        User user = prt.getUser();
        if (user == null) {
            throw new InvalidTokenException();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        prt.markUsed(now);
        passwordResetTokenRepository.save(prt);
    }
}
