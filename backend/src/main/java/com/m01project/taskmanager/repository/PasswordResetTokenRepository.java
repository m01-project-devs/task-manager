package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(UUID token);

    Optional<PasswordResetToken> findTopByUserIdAndUsedAtIsNullOrderByCreatedAtDesc(Long userId);

    Optional<PasswordResetToken> findByTokenAndUsedAtIsNull(UUID token);
}
