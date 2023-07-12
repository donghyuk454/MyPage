package com.mong.project.repository.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CommentRepositoryImplTest {

    @Autowired private CommentRepository commentRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("새로운 comment 를 저장합니다. 그리고 해당 comment 를 반환합니다.")
    void createComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));

        //when
        Comment comment = createNewComment(member, board);
        Comment result = commentRepository.save(comment);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(member).isEqualTo(result.getMember());
        assertThat(board).isEqualTo(result.getBoard());
        assertThat(board.getComments()).contains(result);
    }

    private static Comment createNewComment(Member member, Board board) {
        return Comment.builder()
                .content("content")
                .member(member)
                .board(board)
                .build();
    }

    @Test
    @DisplayName("comment_id 를 통해 comment 를 조회합니다. 조회한 comment 를 반환합니다.")
    void getComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        Comment comment = commentRepository.save(createNewComment(member, board));

        //when
        Comment result = commentRepository.findById(comment.getId()).get();

        //then
        assertThat(result).isEqualTo(comment);
        assertThat(result.getBoard()).isEqualTo(board);
        assertThat(result.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("comment_id 를 통해 comment 를 삭제합니다. 이 때, board 의 comments 에서도 삭제되었는지 확인합니다.")
    void deleteComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        Comment comment = commentRepository.save(createNewComment(member, board));

        //when
        commentRepository.deleteById(comment.getId());
        em.flush();
        em.clear();

        //then

        assertThat(board.getComments()).isEmpty();
    }

    private static Board createBoard(Member member) {
        return Board.builder()
                .title("test title")
                .content("test content")
                .member(member)
                .build();
    }

    private static Member createMember() {
        return Member.builder()
                .name("test")
                .alias("test")
                .email("test@test.com")
                .passwd("testpasswd")
                .build();
    }
}