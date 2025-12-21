package com.m01project.taskmanager.service;

import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.m01project.taskmanager.domain.TodoItem;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final BoardRepository boardRepository;

    public TodoItemResponse createTodoItem(TodoItemRequest request, Long boardId){

        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setCompleted(false);
        todoItem.setBoard(board);

        TodoItem saved = todoItemRepository.save(todoItem);
        return new TodoItemResponse(saved.getTitle(), saved.getId(), saved.getDescription(), saved.isCompleted());
    }

    @Transactional
    public TodoItemResponse updateToDoItem(Long boardId, Long itemId, TodoItemRequest request) {
        TodoItem item = todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(itemId, boardId)
                .orElseThrow(() -> new RuntimeException("Todo item is not found for this board"));


        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()){
                throw new IllegalArgumentException("Title cannot be empty!");
            }
            item.setTitle(request.getTitle());
        }

        if (request.getDescription() != null){
            item.setDescription(request.getDescription());
        }

        TodoItem saved = todoItemRepository.save(item);

        return new TodoItemResponse(
                saved.getTitle(),
                saved.getId(),
                saved.getDescription(),
                saved.isCompleted()
        );
    }

    @Transactional
    public List<TodoItemResponse> getBoardItems(Long boardId){

        if (!boardRepository.existsById(boardId)) {
            throw new ResourceNotFoundException("Board not found");
        }

        return todoItemRepository
                .findByBoardIdAndDeletedAtIsNullOrderByIdAsc(boardId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

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
