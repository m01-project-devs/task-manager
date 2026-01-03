package com.m01project.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m01project.taskmanager.domain.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "users")
public class User implements UserDetails {

    public User(String email, String password, String firstName, String lastName) {
        this(null, email, password, firstName, lastName, null, null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

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

    @Enumerated(EnumType.STRING)
    @Column
    private Role role = Role.USER; // default user role.

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

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
