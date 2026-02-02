package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import org.springframework.data.domain.Page;

public interface TodoItemService {
    TodoItemResponse createTodoItem(TodoItemRequest request, Long boardId, User user);
    TodoItemResponse getTodoItem(Long boardId, Long itemId, User user);
    TodoItemResponse updateToDoItem(Long boardId, Long itemId, TodoItemRequest request, User user);
    Page<TodoItemResponse> getBoardItems(Long boardId, int page, int size, User user);
    void deleteTodoItem(Long boardId, Long itemId, User user);
}
