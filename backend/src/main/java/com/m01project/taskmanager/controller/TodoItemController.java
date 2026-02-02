package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import com.m01project.taskmanager.service.TodoItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/todo")
public class TodoItemController {

    private final TodoItemService todoItemService;
    public TodoItemController (TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Page<TodoItemResponse>> getTodoItems(@PathVariable Long boardId,
                                                               @RequestParam (defaultValue = "0") int page,
                                                               @RequestParam (defaultValue = "10") int size,
                                                               @AuthenticationPrincipal User user) {
        Page<TodoItemResponse> todos = todoItemService.getBoardItems(boardId, page, size, user);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<TodoItemResponse> create(@PathVariable Long boardId,
                                                   @Valid @RequestBody TodoItemRequest request,
                                                   @AuthenticationPrincipal User user) {
        TodoItemResponse response = todoItemService.createTodoItem(request, boardId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{boardId}/{todoId}")
    public ResponseEntity<TodoItemResponse> update(@PathVariable Long boardId,
                                                   @PathVariable Long todoId,
                                                   @Valid @RequestBody TodoItemRequest request,
                                                   @AuthenticationPrincipal User user) {
        TodoItemResponse response = todoItemService.updateToDoItem(boardId, todoId, request, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{boardId}/{todoId}")
    public void delete(@PathVariable Long boardId,
                       @PathVariable Long todoId,
                       @AuthenticationPrincipal User user) {
        todoItemService.deleteTodoItem(boardId, todoId, user);
    }
}
