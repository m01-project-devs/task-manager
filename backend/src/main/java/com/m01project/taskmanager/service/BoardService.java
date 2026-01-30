package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.BoardRequest;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.exception.BoardNotFoundException;
import com.m01project.taskmanager.exception.DuplicateBoardTitleException;
import com.m01project.taskmanager.repository.BoardRepository;
import com.m01project.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardResponse createBoard(BoardRequest request, User user);
    Page<BoardResponse> getBoards(User user, Pageable pageable);
    void deleteBoard(Long boardId, User user);
    BoardResponse updateBoard(Long boardId, BoardRequest request, User user);
}
