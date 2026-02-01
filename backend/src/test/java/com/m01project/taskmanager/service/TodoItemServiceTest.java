package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.TodoItem;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.TodoItemRequest;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import com.m01project.taskmanager.service.impl.TodoItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TodoItemServiceImpl todoItemService;

    @Test
    void shouldReturnTodoItem_whenExists() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("test");
        item.setBoard(board);

        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(item));
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(board));

        TodoItemResponse response = todoItemService.getTodoItem(1L, 10L, user);

        assertEquals("test", response.getTitle());
        assertEquals(10L, response.getId());
    }

    @Test
    void getTodoItem_whenMissing_shouldThrowException() {
        User user = new User();
        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.empty());
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(new Board()));

        assertThatThrownBy(() -> todoItemService.getTodoItem(1L, 10L, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Todo item not found for this board");
    }

    @Test
    void createTodoItem_whenBoardMissing_shouldThrowException() {
        User user = new User();
        TodoItemRequest request = new TodoItemRequest();
        request.setTitle("New Task");
        request.setDescription("Desc");

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoItemService.createTodoItem(request, 1L, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Board not found");
        verify(todoItemRepository, never()).save(any());
    }

    @Test
    void createTodoItem_shouldTrimTitleAndSave() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItemRequest request = new TodoItemRequest();
        request.setTitle("  New Task  ");
        request.setDescription("Desc");

        TodoItem saved = new TodoItem();
        saved.setId(7L);
        saved.setTitle("New Task");
        saved.setDescription("Desc");
        saved.setCompleted(false);
        saved.setBoard(board);

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user)).thenReturn(Optional.of(board));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(saved);

        TodoItemResponse response = todoItemService.createTodoItem(request, 1L, user);

        assertThat(response.getTitle()).isEqualTo("New Task");
        assertThat(response.getDescription()).isEqualTo("Desc");
        assertThat(response.isCompleted()).isFalse();
        verify(todoItemRepository).save(any(TodoItem.class));
    }

    @Test
    void updateTodoItem_whenMissing_shouldThrowException() {
        User user = new User();
        TodoItemRequest request = new TodoItemRequest();
        request.setTitle("Updated");

        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.empty());
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(new Board()));

        assertThatThrownBy(() -> todoItemService.updateToDoItem(1L, 10L, request, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Todo item is not found for this board");
    }

    @Test
    void updateTodoItem_whenBlankTitle_shouldThrowException() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("Old");
        item.setBoard(board);

        TodoItemRequest request = new TodoItemRequest();
        request.setTitle("   ");

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(board));
        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(item));

        assertThatThrownBy(() -> todoItemService.updateToDoItem(1L, 10L, request, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title cannot be empty!");
    }

    @Test
    void updateTodoItem_shouldUpdateFields() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("Old");
        item.setDescription("Old Desc");
        item.setBoard(board);
        item.setCompleted(false);

        TodoItemRequest request = new TodoItemRequest();
        request.setTitle(" Updated ");
        request.setDescription("New Desc");

        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(item));
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(board));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(item);

        TodoItemResponse response = todoItemService.updateToDoItem(1L, 10L, request, user);

        assertThat(response.getTitle()).isEqualTo("Updated");
        assertThat(response.getDescription()).isEqualTo("New Desc");
        verify(todoItemRepository).save(any(TodoItem.class));
    }

    @Test
    void getBoardItems_whenBoardMissing_shouldThrowException() {
        User user = new User();
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoItemService.getBoardItems(1L, 0, 5, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Board not found");
    }

    @Test
    void getBoardItems_shouldReturnPagedItems() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("Task");
        item.setDescription("Desc");
        item.setCompleted(false);
        item.setBoard(board);

        Pageable pageable = PageRequest.of(0, 5);
        Page<TodoItem> page = new PageImpl<>(List.of(item), pageable, 1);

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user)).thenReturn(Optional.of(board));
        when(todoItemRepository.findByBoardIdAndDeletedAtIsNullOrderByIdAsc(1L, pageable))
                .thenReturn(page);

        Page<TodoItemResponse> result = todoItemService.getBoardItems(1L, 0, 5, user);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Task");
    }

    @Test
    void deleteTodoItem_whenMissing_shouldThrowException() {
        User user = new User();
        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.empty());
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(new Board()));

        assertThatThrownBy(() -> todoItemService.deleteTodoItem(1L, 10L, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Todo item not found for this board");
    }

    @Test
    void deleteTodoItem_shouldSetDeletedAt() {
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("Task");
        item.setBoard(board);

        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(item));
        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(board));

        todoItemService.deleteTodoItem(1L, 10L, user);

        assertThat(item.getDeletedAt()).isNotNull();
        assertThat(item.getDeletedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(todoItemRepository).save(item);
    }
}
