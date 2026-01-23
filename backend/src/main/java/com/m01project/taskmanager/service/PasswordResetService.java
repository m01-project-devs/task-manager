package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.PasswordResetToken;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.exception.InvalidTokenException;
import com.m01project.taskmanager.exception.TokenAlreadyUsedException;
import com.m01project.taskmanager.exception.TokenExpiredException;
import com.m01project.taskmanager.repository.PasswordResetTokenRepository;
import com.m01project.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final int TOKEN_EXPIRES_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    // Email enumeration önlemek için: user yoksa bile sessiz dön
    public void forgotPassword(String email){
        userRepository.findByEmail(email).ifPresent(user -> {
            PasswordResetToken token = PasswordResetToken.builder()
                    .user(user)
                    .token(UUID.randomUUID())
                    .expiresAt(LocalDateTime.now().plusMinutes(TOKEN_EXPIRES_MINUTES))
                    .usedAt(null)
                    .build();
            passwordResetTokenRepository.save(token);
        });
    }

    public void resetPassword(String token, String newPassword) {

        final UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(token);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException();
        }

        PasswordResetToken prt = passwordResetTokenRepository.findByToken(tokenUuid)
                .orElseThrow(() -> new InvalidTokenException());

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
