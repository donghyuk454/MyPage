package com.mong.project.repository.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;

import java.util.Optional;

public interface BoardRepository {
    Board save(Member member, Board board);
    Board save(Board board);
    Optional<Board> findById(Long id);
    void deleteBoardById(Long id);
}
