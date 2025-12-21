package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem,Long> {

    Optional<TodoItem> findByIdAndBoardIdAndDeletedAtIsNull(Long id, Long boardId);

    List<TodoItem> findByBoardIdAndDeletedAtIsNullOrderByIdAsc(Long boardId);

}
