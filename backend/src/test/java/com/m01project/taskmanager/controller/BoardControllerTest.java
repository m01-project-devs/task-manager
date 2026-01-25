package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.BoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardControllerTest {

    @Mock
    private BoardService boardService; // Mocked service

    @InjectMocks
    private BoardController boardController; // Controller with mock injected

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testUpdateBoard_success() {
        // 1️⃣ Input
        Long boardId = 1L;
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BoardRequest request = new BoardRequest();
        request.setTitle("Updated Board");

        // 2️⃣ Mock service response
        BoardResponse mockResponse = mock(BoardResponse.class);
        when(mockResponse.getName()).thenReturn("Updated Board");

        when(boardService.updateBoard(boardId, request, user)).thenReturn(mockResponse);

        // 3️⃣ Call controller
        ResponseEntity<BoardResponse> response = boardController.updateBoard(boardId, request, user);

        // 4️⃣ Verify response
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Updated Board", response.getBody().getName());

        // 5️⃣ Verify service was called once
        verify(boardService, times(1)).updateBoard(boardId, request, user);
    }

    @Test
    public void testUpdateBoard_invalidRequest() {
        Long boardId = 1L;
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        BoardRequest request = new BoardRequest(); // title is null / empty

        // Mock the service to throw an exception if invalid
        when(boardService.updateBoard(boardId, request, user))
                .thenThrow(new IllegalArgumentException("Board name must not be empty"));

        // Call controller and assert exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardController.updateBoard(boardId, request, user);
        });

        // Optional: check exception message
        assertEquals("Board name must not be empty", exception.getMessage());
    }

}
