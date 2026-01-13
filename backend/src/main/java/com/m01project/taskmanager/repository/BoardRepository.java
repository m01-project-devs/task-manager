package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByUserAndDeletedFalse(User user, Pageable pageable);

    Optional<Board> findByIdAndUserAndDeletedFalse(Long id, User user);

    boolean existsByUserAndTitleAndDeletedFalse(User user, String title);

}
