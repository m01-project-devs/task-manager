package com.m01project.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable = false)
    private String email;

    @Column (nullable = false)
    @JsonIgnore
    private String password;

    @Column (nullable = false, name = "first_name")
    private String firstName;

    @Column (nullable = false, name = "last_name")
    private String lastName;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * @PrePersist: This method is automatically called by JPA right before
     * the entity is inserted into the database. It ensures that the
     * createdAt field is always set, even if the value is not provided manually.
     * This makes the entity independent of the database default timestamp
     * and guarantees consistent audit behavior across environments.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
