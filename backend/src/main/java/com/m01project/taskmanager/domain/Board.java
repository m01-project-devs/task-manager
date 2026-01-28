package com.m01project.taskmanager.domain;

import com.m01project.taskmanager.domain.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board",
        indexes = {
                @Index(name = "idx_board_user_id", columnList = "user_id")
        })
public class Board extends BaseEntity {

    @Column(nullable = false, length =100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
