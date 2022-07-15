package com.mong.MyProject.service.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BoardService {

    private BoardRepository boardRepository;
    private MemberRepository memberRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 새로운 board 생성
     * */
    public Board addBoard(Long member_id, Board board){
        Member member = memberRepository.findById(member_id).get();
        return boardRepository.save(member, board);
    }

    /**
     * board 내용(title, content) 수정
     * */
    public Board changeBoard(Long board_id, String title, String content) {
        Board board = boardRepository.findById(board_id).get();
        board.setTitle(title);
        board.setContent(content);
        return boardRepository.save(board);
    }

    /**
     * board 삭제
     * */
    public void deleteBoard(Long board_id) {
        boardRepository.deleteBoardById(board_id);
    }
}
