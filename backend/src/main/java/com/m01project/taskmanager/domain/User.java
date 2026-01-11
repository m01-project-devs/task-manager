package com.m01project.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    /**
     * Convenience constructor for creating a User with basic information.
     * Automatically sets the role to USER by default.
     * This ensures that all users created through this constructor have a proper role
     * without needing to explicitly set it in tests or service layer code.
     *
     * @param email     the user's email address
     * @param password  the user's password (should be encoded before saving)
     * @param firstName the user's first name
     * @param lastName  the user's last name
     */
    public User(String email, String password, String firstName, String lastName) {
        this(null, email, password, firstName, lastName, Role.USER, null);
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

    /**
     * User's role in the system.
     * Defaults to USER role for standard user accounts.
     * This field is initialized with a default value to ensure all users have a role.
     * Note: When using the all-args constructor (@AllArgsConstructor from Lombok),
     * this default value will be overridden if null is passed explicitly.
     * Use the 4-argument convenience constructor to automatically get USER role.
     */
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