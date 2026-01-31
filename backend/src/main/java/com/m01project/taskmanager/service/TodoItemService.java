package com.m01project.taskmanager.service;

import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import org.springframework.data.domain.Page;

public interface TodoItemService {
    TodoItemResponse createTodoItem(TodoItemRequest request, Long boardId);
    TodoItemResponse getTodoItem(Long boardId, Long itemId);
    TodoItemResponse updateToDoItem(Long boardId, Long itemId, TodoItemRequest request);
    Page<TodoItemResponse> getBoardItems(Long boardId, int page, int size);
    void deleteTodoItem(Long boardId, Long itemId);
}
