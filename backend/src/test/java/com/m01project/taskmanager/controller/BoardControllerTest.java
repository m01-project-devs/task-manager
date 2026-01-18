package com.m01project.taskmanager.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.dto.request.UpdateBoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.service.BoardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        UpdateBoardRequest request = new UpdateBoardRequest();
        request.setName("Updated Board");

        // 2️⃣ Mock service response
        Board mockBoard = new Board();
        mockBoard.setId(boardId);
        mockBoard.setName("Updated Board");

        when(boardService.updateBoard(boardId, request)).thenReturn(mockBoard);

        // 3️⃣ Call controller
        BoardResponse response = boardController.updateBoard(boardId, request);

        // 4️⃣ Verify response
        assertNotNull(response);
        assertEquals("Updated Board", response.getName());

        // 5️⃣ Verify service was called once
        verify(boardService, times(1)).updateBoard(boardId, request);
    }

    @Test
    public void testUpdateBoard_invalidRequest() {
        Long boardId = 1L;
        UpdateBoardRequest request = new UpdateBoardRequest(); // name is null / empty

        // Mock the service to throw an exception if invalid
        when(boardService.updateBoard(boardId, request))
                .thenThrow(new IllegalArgumentException("Board name must not be empty"));

        // Call controller and assert exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardController.updateBoard(boardId, request);
        });

        // Optional: check exception message
        assertEquals("Board name must not be empty", exception.getMessage());
    }

}
