package com.mong.project.repository.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.board.BoardStatus;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.member.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardRepositoryImplTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 board 에 영속성을 부여합니다.")
    void addBoard() {
        //given
        Member member = newTestMember();

        //when
        Board board = boardRepository.save(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        //then
        assertThat(board.getMember()).isEqualTo(member);
        assertThat(member.getBoards().size()).isEqualTo(1);
        assertThat(member.getBoards().get(0).getId()).isNotNull();
    }

    @Test
    @DisplayName("board 의 내용을 수정합니다. member 에서 board 를 가져와 수정하고 저장할 때 문제가 없는지 확인합니다.")
    void changeBoard() {
        //given
        Member member = newTestMember();
        boardRepository.save(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        //when
        Board board = member.getBoards().get(0);
        board.setContent("수정된 내용입니다");
        board.setTitle("수정된 제목입니다");
        boardRepository.save(board);

        Board result1 = boardRepository.findById(board.getId()).get();
        Board result2 = member.getBoards().get(0);

        //then
        assertThat(result1).isNotNull();
        assertThat(result2).isEqualTo(result1);
    }

    @Test
    @DisplayName("board id 를 통해 board 를 조회합니다.")
    void getBoard() {
        //given
        Member member = newTestMember();
        boardRepository.save(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());

        Board board = member.getBoards().get(0);

        //when
        Board result = boardRepository.findById(board.getId()).get();

        //then
        assertThat(result.getContent()).isEqualTo(board.getContent());
        assertEquals(board.getTitle(), result.getTitle());
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("board_id 를 통해 board 를 삭제합니다. member 의 boards 에서도 삭제가 되는지 확인합니다.")
    void deleteBoard() {
        //given
        Member member = newTestMember();
        boardRepository.save(member, Board.builder()
                .title("제목입니다").content("내용입니다.").build());
        Board board = member.getBoards().get(0);

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