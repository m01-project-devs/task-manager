package com.m01project.taskmanager.domain;

import com.m01project.taskmanager.domain.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board",
        indexes = {
                @Index(name = "idx_board_user_id", columnList = "user_id"),
                @Index(name = "idx_board_is_deleted", columnList = "is_deleted")
        })
public class Board extends BaseEntity {

    @Column(nullable = false, length =100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
