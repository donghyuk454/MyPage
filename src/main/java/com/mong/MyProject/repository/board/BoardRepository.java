package com.mong.MyProject.repository.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;

import java.util.Optional;

public interface BoardRepository {
    Board create(Member member, Board board);
    Optional<Board> findById(Long id);
    void deleteBoardById(Long id);
}
