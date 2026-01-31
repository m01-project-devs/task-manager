package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.BoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.exception.BoardNotFoundException;
import com.m01project.taskmanager.exception.DuplicateBoardTitleException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.TodoItemRepository;
import com.m01project.taskmanager.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final TodoItemRepository todoItemRepository;

    @Override
    @Transactional
    public BoardResponse createBoard(BoardRequest request, User user) {
        if (boardRepository.existsByUserAndTitleAndDeletedAtIsNull(user, request.getTitle())) {
            throw new DuplicateBoardTitleException(request.getTitle());
        }

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setUser(user);

        Board saved = boardRepository.save(board);
        return new BoardResponse(saved);
    }

    @Override
    public Page<BoardResponse> getBoards(User user, Pageable pageable) {
        return boardRepository
                .findByUserAndDeletedAtIsNull(user, pageable)
                .map(BoardResponse::new);
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository
                .findByIdAndUserAndDeletedAtIsNull(boardId, user)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        // should also soft delete all related TodoItems related with this board too.
        todoItemRepository.findByBoardAndDeletedAtIsNull(board).forEach(todoItem -> {
            todoItem.setDeletedAt(LocalDateTime.now());
        });

        board.setDeletedAt(LocalDateTime.now());
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request, User user) {
        Board board = boardRepository
                .findByIdAndUserAndDeletedAtIsNull(boardId, user)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (!board.getTitle().equals(request.getTitle())
                && boardRepository.existsByUserAndTitleAndDeletedAtIsNull(user, request.getTitle())) {
            throw new DuplicateBoardTitleException(request.getTitle());
        }

        board.setTitle(request.getTitle());
        Board updated = boardRepository.save(board);
        return new BoardResponse(updated);
    }
}
