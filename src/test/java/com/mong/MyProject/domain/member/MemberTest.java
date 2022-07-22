package com.mong.MyProject.domain.member;

import com.mong.MyProject.domain.board.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    Member member;

    @BeforeEach
    void beforeEach() {
        //given
        member = Member.builder()
                .name("name")
                .passwd("passwd")
                .alias("alias")
                .email("email@email.com")
                .build();
    }

    @AfterEach
    void afterEach(){
        member = null;
    }

    @Test
    @DisplayName("member 가 builder 를 통해 생성되는지 확인합니다.")
    void checkBuilder() {
        //when
        Member member1 = new Member();
        member1.setName("name");
        member1.setPasswd("passwd");
        member1.setAlias("alias");
        member1.setEmail("email@email.com");

        //then
        assertThat(member.getAlias()).isEqualTo(member1.getAlias());
        assertThat(member.getPasswd()).isEqualTo(member1.getPasswd());
        assertThat(member.getEmail()).isEqualTo(member1.getEmail());
        assertThat(member.getName()).isEqualTo(member1.getName());

        assertThat(member.getCreatedDateTime()).isNotNull();
        assertThat(member.getCreatedDateTime()).isEqualTo(member.getLastModifiedDateTime());
        assertThat(member.getDeletedDateTime()).isNull();
    }

    @Test
    @DisplayName("setPasswd 실행 시 lastModifiedDateTime 도 바뀌는 지 확인합니다.")
    void changePassword(){
        //when
        LocalDateTime lmdt = member.getLastModifiedDateTime();
        try{
            Thread.sleep(1000);
            member.setPasswd("newPasswd");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //then
            assertThat(member.getLastModifiedDateTime()).isNotEqualTo(lmdt);
        }
    }

    @Test
    @DisplayName("member 를 삭제합니다. 삭제 시 deletedDateTime 이 입력되는지 확인합니다.")
    void deleteMember(){
        //when
        member.delete();

        //then
        assertThat(member.getDeletedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("member 에 board 를 추가합니다.")
    void addBoard(){
        //when
        member.addBoard(Board.builder().build());

        //then
        assertThat(member.getBoards().size()).isNotZero();
    }

    @Test
    @DisplayName("member 에 board 를 삭제합니다.")
    void deleteBoard(){
        //given
        Board board = Board.builder().build();
        member.addBoard(board);

        //when
        member.deleteBoard(board);

        //then
        assertThat(member.getBoards().size()).isZero();
    }
}