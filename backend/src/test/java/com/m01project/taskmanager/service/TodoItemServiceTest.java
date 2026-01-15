package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.TodoItem;
import com.m01project.taskmanager.dto.response.TodoItemResponse;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    @Test
    void shouldReturnTodoItem_whenExists() {
        Board board = new Board();
        board.setId(1L);

        TodoItem item = new TodoItem();
        item.setId(10L);
        item.setTitle("test");
        item.setBoard(board);

        when(todoItemRepository.findByIdAndBoardIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(item));

        TodoItemResponse response = todoItemService.getTodoItem(1L, 10L);

        assertEquals("test", response.getTitle());
        assertEquals(10L, response.getId());
    }
}
