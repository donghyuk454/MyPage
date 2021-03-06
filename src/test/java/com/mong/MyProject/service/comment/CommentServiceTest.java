package com.mong.MyProject.service.comment;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.comment.Comment;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.comment.CommentRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import com.mong.MyProject.service.board.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private Comment comment;
    @Mock
    private Board board;

    @Test
    @DisplayName("comment 를 추가합니다.")
    void addComment() {
        Member member = mock(Member.class);

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));
        when(boardRepository.findById(1L))
                .thenReturn(Optional.of(board));
        when(commentRepository.save(member, board, "content"))
                .thenReturn(comment);

        commentService.addComment(1L, 1L, "content");

        verify(commentRepository, times(1))
                .save(member, board, "content");
    }

    @Test
    @DisplayName("comment 의 content 내용을 수정합니다.")
    void changeComment() {
        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));
        when(comment.getContent())
                .thenReturn("changed");

        Comment result = commentService.changeComment(1L, "changed");

        verify(commentRepository, times(1))
                .findById(1L);
        verify(comment, times(1))
                .setContent("changed");
        assertThat(result.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    @DisplayName("comment 를 삭제합니다.")
    void deleteComment() {
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1))
                .deleteById(1L);
    }
}