package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByUserAndDeletedAtIsNull(User user, Pageable pageable);

    Optional<Board> findByIdAndUserAndDeletedAtIsNull(Long id, User user);

    boolean existsByUserAndTitleAndDeletedAtIsNull(User user, String title);

}
