package com.mong.project.domain.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    Member member;
    Board board;
    Comment comment;

    @BeforeEach
    void beforeEach(){
        //given
        member = Member.builder().build();
        board = Board.builder()
                .content("content")
                .title("title")
                .member(member)
                .build();
        comment = Comment.builder()
                .content("content")
                .board(board)
                .member(member)
                .build();
    }

    @AfterEach
    void afterEach(){
        comment = null;
        board = null;
        member = null;
    }

    @Test
    @DisplayName("comment 를 builder 를 통해 생성합니다.")
    void checkBuilder() {
        //when
        //then
        assertThat(comment).isNotNull();
        assertThat(comment.getContent()).isEqualTo("content");
        assertThat(comment.getMember()).isNotNull();
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getBoard()).isEqualTo(board);
        assertThat(comment.getCreatedDateTime()).isNotNull();
        assertThat(comment.getLastModifiedDateTime()).isNotNull();
        assertThat(board.getComments().size()).isNotZero();
    }

    @Test
    @DisplayName("comment 의 content 를 수정합니다.")
    void changeComment() {
        //given
        LocalDateTime lmdt = comment.getLastModifiedDateTime();
        try{
            //when
            Thread.sleep(1000);
            comment.setContent("newContent");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //then
            assertThat(comment.getContent()).isEqualTo("newContent");
            assertThat(comment.getLastModifiedDateTime()).isNotEqualTo(lmdt);
        }
    }
}