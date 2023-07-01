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
class BoardRepositoryImplTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;
    @PersistenceContext private EntityManager em;

    @Test
    @DisplayName("새로운 board 에 영속성을 부여합니다.")
    void addBoard() {
        //given
        Member member = newTestMember();

        //when
        member = memberRepository.getById(memberId);
        Board board = newTestBoard();
        member.addBoard(board);

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
        member.addBoard(newTestBoard());
        memberRepository.save(member);

        //when
        Board board = member.getBoards().get(0);
        board.setContent("수정된 내용입니다");
        board.setTitle("수정된 제목입니다");
        boardRepository.save(board);

        Board result1 = boardRepository.findById(board.getId()).get();
        Board result2 = member.getBoards().get(0);

        //then
        assertThat(result1).isNotNull();
        isEqualBoard(result1, result2);
    }

    private static void isEqualBoard(Board result1, Board result2) {
        assertThat(result2.getId()).isEqualTo(result1.getId());
        assertThat(result2.getTitle()).isEqualTo(result1.getTitle());
        assertThat(result2.getContent()).isEqualTo(result1.getContent());
    }


    @Test
    @DisplayName("board id 를 통해 board 를 조회합니다.")
    void getBoard() {
        //given
        Member member = newTestMember();
        member.addBoard(newTestBoard());
        memberRepository.save(member);
        Board board = member.getBoards().get(0);

        //when
        Board result = boardRepository.findById(board.getId()).get();

        //then
        isEqualBoard(result, board);
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("board_id 를 통해 board 를 삭제합니다. member 의 boards 에서도 삭제가 되는지 확인합니다.")
    void deleteBoard() {
        //given
        Member member = newTestMember();
        Board board = newTestBoard();
        member.addBoard(board);

        //when
        boardRepository.deleteBoardById(board.getId());

        //then
        assertThat(member.getBoards().size()).isEqualTo(0);
        assertThat(board.getStatus()).isEqualTo(BoardStatus.DELETED);
        assertThat(board.getDeletedDateTime()).isNotNull();
    }

    private Member newTestMember(){
        Member member = memberRepository.save(Member.builder()
                .name("name").email("tt@test.com").alias("테스트 닉네임").passwd("passwd").build());
        em.flush();
        em.clear();
        return member;
    }

    private Board newTestBoard() {
        return boardRepository.save(Board.builder()
                .title("제목입니다").content("내용입니다.").build());
    }
}