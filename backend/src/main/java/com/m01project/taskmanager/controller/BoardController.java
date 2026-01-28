package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.CreateBoardRequest;
import com.m01project.taskmanager.dto.request.BoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE BOARD
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user) {

        BoardResponse response = boardService.createBoard(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LIST BOARDS
    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getBoards(
            @AuthenticationPrincipal User user,
            Pageable pageable) {

        return ResponseEntity.ok(boardService.getBoards(user, pageable));
    }

    // DELETE BOARD
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal User user) {

        boardService.deleteBoard(boardId, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(boardService.updateBoard(id, request, user));
    }

}
