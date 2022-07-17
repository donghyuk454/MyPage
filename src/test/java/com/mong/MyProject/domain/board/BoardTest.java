package com.mong.MyProject.domain.board;

import com.mong.MyProject.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;

    @BeforeEach
    void beforeEach(){
        //given
        Member member = Member.builder().build();
        board = Board.builder()
                .content("content")
                .title("title")
                .member(member)
                .build();
    }

    @AfterEach
    void afterEach(){
        board = null;
    }

    @Test
    @DisplayName("board 를 builder 를 통해 생성합니다.")
    void 보드_생성() {
        //when
        //then
        assertThat(board).isNotNull();
        assertThat(board.getMember()).isNotNull();
        assertThat(board.getMember().getBoards().size()).isNotZero();
        assertThat(board.getStatus()).isEqualTo(BoardStatus.ACTIVE);
        assertThat(board.getCreatedDateTime()).isNotNull();
        assertThat(board.getLastModifiedDateTime()).isNotNull();
        assertThat(board.getComments().size()).isZero();
    }

    @Test
    @DisplayName("board 의 title 과 content 를 수정합니다.")
    void 보드_제목_내용_수정() {
        //given
        LocalDateTime lmdt = board.getLastModifiedDateTime();
        try{
            //when
            Thread.sleep(1000);
            board.setContent("newContent");
            board.setTitle("newTitle");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //then
            assertThat(board.getContent()).isEqualTo("newContent");
            assertThat(board.getTitle()).isEqualTo("newTitle");
            assertThat(board.getLastModifiedDateTime()).isNotEqualTo(lmdt);
        }
    }

    @Test
    @DisplayName("board 를 삭제합니다. member 에서도 board 가 삭제되었는지 확인합니다.")
    void 보드_삭제() {
        //giver
        Member member = board.getMember();

        //when
        board.delete();

        //then
        assertThat(member.getBoards().size()).isZero();
        assertThat(board.getDeletedDateTime()).isNotNull();
    }
}