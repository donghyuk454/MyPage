package com.mong.project.repository.board;

import com.mong.project.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    void deleteBoardById(Long boardId);
}
