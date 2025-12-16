package com.m01project.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();
    }

}
