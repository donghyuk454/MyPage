package com.mong.MyProject.repository.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.board.BoardStatus;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardRepositoryImplTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 board 에 영속성을 부여합니다.")
    void create() {
        //given
        Member member = newTestMember();

        //when
        Board board = boardRepository.create(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        //then
        assertThat(board.getMember()).isEqualTo(member);
        assertThat(member.getBoards().size()).isEqualTo(1);
        assertThat(member.getBoards().stream().findAny().get()).isEqualTo(board);
    }

    @Test
    @DisplayName("board id 를 통해 board 를 조회합니다.")
    void findById() {
        //given
        Member member = newTestMember();
        Board board = boardRepository.create(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        //when
        Board result = boardRepository.findById(board.getId()).get();

        //then
        assertThat(result).isEqualTo(board);
        assertThat(result.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("board_id 를 통해 board 를 삭제합니다. member 의 boards 에서도 삭제가 되는지 확인합니다.")
    void deleteBoard() {
        //given
        Member member = newTestMember();
        Board board = boardRepository.create(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        //when
        boardRepository.deleteBoardById(board.getId());

        //then
        assertThat(member.getBoards().size()).isEqualTo(0);
        assertThat(board.getStatus()).isEqualTo(BoardStatus.DELETED);
        assertThat(board.getDeletedDateTime()).isNotNull();
    }

    private Member newTestMember(){
        return memberRepository.save(Member.builder()
                .name("name").email("tt@test.com").alias("테스트 닉네임").passwd("passwd").build());
    }
}