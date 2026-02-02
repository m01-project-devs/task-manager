package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.Role;
import com.m01project.taskmanager.domain.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    boolean existsByEmailAndDeletedAtIsNull(String email);
    Page<User> findAllByRoleEqualsAndDeletedAtIsNull(Role role, @NonNull Pageable pageable);

    @Query("""
SELECT u FROM User u
WHERE u.deletedAt IS NULL AND
(
   LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))
) AND u.role = :role
""")
    Page<User> search(@Param("q") String q, @Param("role") Role role, Pageable pageable);

    Page<User> findAllByDeletedAtIsNull(@NonNull Pageable pageable);
}
