package com.mong.MyProject.repository.comment;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.comment.Comment;
import com.mong.MyProject.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CommentRepositoryImplTest {

    @Autowired private CommentRepository commentRepository;

    @Test
    @DisplayName("새로운 comment 를 저장합니다. 그리고 해당 comment 를 반환합니다.")
    void 새로운_댓글() {
        //given
        Member member = Member.builder().build();
        Board board = Board.builder().build();

        //when
        Comment comment = commentRepository.save(member, board, "content");

        //then
        assertThat(comment.getId()).isNotNull();
        assertThat(member).isEqualTo(comment.getMember());
        assertThat(board).isEqualTo(comment.getBoard());
        assertThat(board.getComments().contains(comment)).isTrue();
    }

    @Test
    @DisplayName("comment_id 를 통해 comment 를 조회합니다. 조회한 comment 를 반환합니다.")
    void 댓글_찾기() {
        //given
        Member member = Member.builder().build();
        Board board = Board.builder().build();
        Comment comment = commentRepository.save(member, board, "content");

        //when
        Comment result = commentRepository.findById(comment.getId()).get();

        //then
        assertThat(result).isEqualTo(comment);
        assertThat(result.getBoard()).isEqualTo(board);
        assertThat(result.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("comment_id 를 통해 comment 를 삭제합니다. 이 때, board 의 comments 에서도 삭제되었는지 확인합니다.")
    void 댓글_삭제() {
        //given
        Member member = Member.builder().build();
        Board board = Board.builder().build();
        Comment comment = commentRepository.save(member, board, "content");

        //when
        commentRepository.deleteById(comment.getId());

        //then
        assertThat(board.getComments().size()).isZero();

    }
}