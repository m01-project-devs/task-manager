package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.TodoItem;
import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import com.m01project.taskmanager.service.TodoItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public TodoItemResponse createTodoItem(TodoItemRequest request, Long boardId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(request.getTitle().trim());
        todoItem.setDescription(request.getDescription());
        todoItem.setCompleted(false);
        todoItem.setBoard(board);

        TodoItem saved = todoItemRepository.save(todoItem);
        return new TodoItemResponse(saved.getTitle(), saved.getId(), saved.getDescription(), saved.isCompleted());
    }

    @Override
    @Transactional
    public TodoItemResponse getTodoItem(Long boardId, Long itemId) {
        TodoItem item = todoItemRepository
                .findByIdAndBoardIdAndDeletedAtIsNull(itemId, boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo item not found for this board"));
        return mapToResponse(item);
    }

    @Override
    @Transactional
    public TodoItemResponse updateToDoItem(Long boardId, Long itemId, TodoItemRequest request) {
        TodoItem item = todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(itemId, boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo item is not found for this board"));

        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) {
                throw new IllegalArgumentException("Title cannot be empty!");
            }
            item.setTitle(request.getTitle().trim());
        }

        item.setDescription(request.getDescription());
        item.setCompleted(request.isCompleted());

        TodoItem saved = todoItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Override
    public Page<TodoItemResponse> getBoardItems(Long boardId, int page, int size) {
        if (!boardRepository.existsByIdAndDeletedAtIsNull(boardId)) {
            throw new ResourceNotFoundException("Board not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<TodoItem> items = todoItemRepository.findByBoardIdAndDeletedAtIsNullOrderByIdAsc(boardId, pageable);

        return items.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteTodoItem(Long boardId, Long itemId) {
        TodoItem item = todoItemRepository
                .findByIdAndBoardIdAndDeletedAtIsNull(itemId, boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo item not found for this board"));

        item.setDeletedAt(LocalDateTime.now());
        todoItemRepository.save(item);
    }

    private TodoItemResponse mapToResponse(TodoItem item) {
        return new TodoItemResponse(
                item.getTitle(),
                item.getId(),
                item.getDescription(),
                item.isCompleted()
        );
    }
}
