package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.Board;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.response.BoardResponse;
import com.m01project.taskmanager.dto.request.CreateBoardRequest;
import com.m01project.taskmanager.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void createBoard_shouldSaveBoard() {
        User user = new User();

        CreateBoardRequest request = new CreateBoardRequest();
        request.setName("My Board");

        Board savedBoard = new Board();
        savedBoard.setId(1L);
        savedBoard.setName("My Board");
        savedBoard.setUser(user);

        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        BoardResponse response = boardService.createBoard(request, user);

        assertThat(response.getName()).isEqualTo("My Board");
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void getBoards_shouldReturnBoards() {
        User user = new User();

        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");

        Page<Board> page = new PageImpl<>(
                List.of(board),
                PageRequest.of(0, 10),
                1
        );

        when(boardRepository.findByUserAndDeletedFalse(eq(user), any(Pageable.class)))
                .thenReturn(page);

        Page<BoardResponse> result = boardService.getBoards(user, 0, 10);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Board 1");
    }

    @Test
    void deleteBoard_shouldSoftDelete() {
        User user = new User();

        Board board = new Board();
        board.setId(1L);
        board.setDeleted(false);

        when(boardRepository.findByIdAndUserAndDeletedFalse(1L, user))
                .thenReturn(Optional.of(board));

        boardService.deleteBoard(1L, user);

        assertThat(board.isDeleted()).isTrue();
        verify(boardRepository).save(board);
    }

    @Test
    void deleteBoard_whenNotFound_shouldThrowException() {
        User user = new User();

        when(boardRepository.findByIdAndUserAndDeletedFalse(1L, user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.deleteBoard(1L, user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Board not found");
    }
}
