package com.mong.MyProject.service.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private Member member;
    @Mock
    private Board board;

    @Test
    @DisplayName("member 의 새로운 board 를 추가합니다.")
    void 보드_추가() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.ofNullable(member));
        when(boardRepository.save(member, board))
                .thenReturn(board);

        boardService.addBoard(1L, board);

        verify(memberRepository, times(1))
                .findById(1L);
        verify(boardRepository, times(1))
                .save(member, board);
    }

    @Test
    @DisplayName("board 의 내용(title, content)를 수정합니다.")
    void 보드_내용_수정() {
        when(boardRepository.save(board))
                .thenReturn(board);
        when(boardRepository.findById(1L))
                .thenReturn(Optional.ofNullable(board));

        Board result = boardService.changeBoard(1L, "title", "content");

        verify(boardRepository, times(1))
                .save(board);
    }

    @Test
    @DisplayName("board 를 삭제합니다.")
    void 보드_삭제() {
        doNothing().when(boardRepository).deleteBoardById(1L);

        boardService.deleteBoard(1L);

        verify(boardRepository, times(1))
                .deleteBoardById(1L);
    }
}