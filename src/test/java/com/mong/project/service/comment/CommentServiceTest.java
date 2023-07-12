package com.mong.project.service.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.comment.CommentRepository;
import com.mong.project.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(commentRepository.save(any()))
                .thenReturn(comment);

        commentService.addComment(1L, 1L, "content");

        verify(commentRepository, times(1))
                .save(any());
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
        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));
        when(comment.getBoard())
                .thenReturn(board);
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(comment);
        when(board.getComments())
                .thenReturn(mockComments);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1))
                .deleteById(1L);
        assertThat(mockComments).isEmpty();
    }
}