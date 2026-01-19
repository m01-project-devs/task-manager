package com.m01project.taskmanager.domain;

import com.m01project.taskmanager.domain.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PasswordResetToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isExpired(LocalDateTime now){
        return expiresAt.isBefore(now);
    }

    public boolean isUsed(){
        return usedAt != null;
    }

    public void markUsed(LocalDateTime now){
        this.usedAt = now;
    }
}
