package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.CreateBoardRequest;
import com.m01project.taskmanager.dto.request.UpdateBoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.exception.BoardNotFoundException;
import com.m01project.taskmanager.exception.DuplicateBoardTitleException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // ✅ CREATE BOARD
    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request, User user) {

        // Check duplicate title
        if (boardRepository.existsByUserAndTitleAndDeletedFalse(user, request.getTitle())) {
            throw new DuplicateBoardTitleException(request.getTitle());
        }

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setUser(user);

        Board saved = boardRepository.save(board);
        return new BoardResponse(saved);
    }

    // ✅ LIST BOARDS (Pageable)
    public Page<BoardResponse> getBoards(User user, Pageable pageable) {
        return boardRepository
                .findByUserAndDeletedFalse(user, pageable)
                .map(BoardResponse::new);
    }

    // ✅ SOFT DELETE BOARD
    @Transactional
    public void deleteBoard(Long boardId, User user) {

        Board board = boardRepository
                .findByIdAndUserAndDeletedFalse(boardId, user)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        board.setDeleted(true);
        boardRepository.save(board);
    }

    // ✅ UPDATE BOARD
    @Transactional
    public BoardResponse updateBoard(Long boardId, UpdateBoardRequest request, User user) {

        // Check if board exists
        Board board = boardRepository
                .findByIdAndUserAndDeletedFalse(boardId, user)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        // Check duplicate title
        if (boardRepository.existsByUserAndTitleAndDeletedFalse(user, request.getTitle())) {
            throw new DuplicateBoardTitleException(request.getTitle());
        }

        // Update title
        board.setTitle(request.getTitle());

        Board updated = boardRepository.save(board);
        return new BoardResponse(updated);
    }
}
