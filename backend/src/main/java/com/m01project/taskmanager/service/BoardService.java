package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.BoardResponse;
import com.m01project.taskmanager.dto.CreateBoardRequest;
import com.m01project.taskmanager.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // CREATE BOARD
    public BoardResponse createBoard(CreateBoardRequest request, User user) {
        Board board = new Board();
        board.setName(request.getName());
        board.setUser(user);

        Board saved = boardRepository.save(board);
        return new BoardResponse(saved);
    }

    // LIST BOARDS
    public Page<BoardResponse> getBoards(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return boardRepository
                .findByUserAndDeletedFalse(user, pageable)
                .map(BoardResponse::new);
    }

    // SOFT DELETE BOARD
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository
                .findByIdAndUserAndDeletedFalse(boardId, user)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        board.setDeleted(true);
        boardRepository.save(board);
    }
}
