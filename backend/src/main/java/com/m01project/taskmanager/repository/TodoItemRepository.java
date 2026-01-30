package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem,Long> {

    Optional<TodoItem> findByIdAndBoardIdAndDeletedAtIsNull(Long id, Long boardId);

    Page<TodoItem> findByBoardIdAndDeletedAtIsNullOrderByIdAsc(Long boardId, Pageable pageable);

    List<TodoItem> findByBoardAndDeletedAtIsNull(Board board);
}
