package com.mong.project.domain.member;

import com.mong.project.domain.board.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        Member member1 = Member.builder()
                .name("name")
                .alias("alias")
                .passwd("passwd")
                .email("email@email.com")
                .build();

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
    @DisplayName("setPasswd 실행 시 password 가 정상적으로 바뀌는 지 확인합니다.")
    void changePassword(){
        //when
        String newPasswd = "newPasswd";
        member.setPasswd(newPasswd);

        //then
        assertThat(member.getPasswd()).isEqualTo(newPasswd);
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
        assertThat(member.getBoards()).isEmpty();
    }
}