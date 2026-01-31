package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.BoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.exception.BoardNotFoundException;
import com.m01project.taskmanager.exception.DuplicateBoardTitleException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import com.m01project.taskmanager.service.impl.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    void createBoard_shouldSaveBoard() {
        User user = new User();

        BoardRequest request = new BoardRequest();
        request.setTitle("My Board");

        Board savedBoard = new Board();
        savedBoard.setId(1L);
        savedBoard.setTitle("My Board");
        savedBoard.setUser(user);

        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        BoardResponse response = boardService.createBoard(request, user);

        assertThat(response.getName()).isEqualTo("My Board");
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void createBoard_whenDuplicateTitle_shouldThrowException() {
        User user = new User();

        BoardRequest request = new BoardRequest();
        request.setTitle("My Board");

        when(boardRepository.existsByUserAndTitleAndDeletedAtIsNull(user, "My Board"))
                .thenReturn(true);

        assertThatThrownBy(() -> boardService.createBoard(request, user))
                .isInstanceOf(DuplicateBoardTitleException.class);

        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    void getBoards_shouldReturnBoards() {
        User user = new User();

        Board board = new Board();
        board.setId(1L);
        board.setTitle("Board 1");
        board.setUser(user);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Board> page = new PageImpl<>(
                List.of(board),
                pageable,
                1
        );

        when(boardRepository.findByUserAndDeletedAtIsNull(eq(user), any(Pageable.class)))
                .thenReturn(page);

        Page<BoardResponse> result = boardService.getBoards(user, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Board 1");
    }

    @Test
    void deleteBoard_shouldSoftDelete() {
        User user = new User();

        Board board = new Board();
        board.setId(1L);

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.of(board));
        when(todoItemRepository.findByBoardAndDeletedAtIsNull(board))
                .thenReturn(Collections.emptyList());

        boardService.deleteBoard(1L, user);

        assertThat(board.getDeletedAt()).isNotNull();
        verify(boardRepository).save(board);
    }

    @Test
    void deleteBoard_whenNotFound_shouldThrowException() {
        User user = new User();

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(1L, user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.deleteBoard(1L, user))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("Board not found with id: 1");
    }

    @Test
    void updateBoard_shouldUpdateTitleAndSave() {
        // Arrange
        Long boardId = 1L;
        User user = new User();
        user.setId(1L);

        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Old Title");

        BoardRequest request = new BoardRequest();
        request.setTitle("New Title");

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(boardId, user))
                .thenReturn(Optional.of(board));

        when(boardRepository.save(any(Board.class)))
                .thenReturn(board);

        // Act
        BoardResponse result = boardService.updateBoard(boardId, request, user);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getName());

        verify(boardRepository).findByIdAndUserAndDeletedAtIsNull(boardId, user);
        verify(boardRepository).save(board);
    }

    @Test
    void updateBoard_whenNotFound_shouldThrowException() {
        Long boardId = 1L;
        User user = new User();

        BoardRequest request = new BoardRequest();
        request.setTitle("New Title");

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(boardId, user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.updateBoard(boardId, request, user))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("Board not found with id: 1");

        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    void updateBoard_whenDuplicateTitle_shouldThrowException() {
        Long boardId = 1L;
        User user = new User();

        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Old Title");

        BoardRequest request = new BoardRequest();
        request.setTitle("New Title");

        when(boardRepository.findByIdAndUserAndDeletedAtIsNull(boardId, user))
                .thenReturn(Optional.of(board));
        when(boardRepository.existsByUserAndTitleAndDeletedAtIsNull(user, "New Title"))
                .thenReturn(true);

        assertThatThrownBy(() -> boardService.updateBoard(boardId, request, user))
                .isInstanceOf(DuplicateBoardTitleException.class);

        verify(boardRepository, never()).save(any(Board.class));
    }

}
