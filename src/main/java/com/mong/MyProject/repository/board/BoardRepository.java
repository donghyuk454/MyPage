package com.mong.MyProject.repository.board;

import com.mong.MyProject.domain.board.Board;

import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
}
