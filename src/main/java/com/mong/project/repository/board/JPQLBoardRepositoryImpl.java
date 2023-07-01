package com.mong.project.repository.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class JPQLBoardRepositoryImpl implements JPQLBoardRepository {

    private final EntityManager em;

    @Override
    public Board save(Member member, Board board) {
        board.setMember(member);
        member.addBoard(board);

        if (member.getId() == null)
            em.persist(member);
        else {
            em.merge(member);
        }
        return board;
    }

    @Override
    public Board save(Board board) {
        if(board.getId() == null)
            em.persist(board);
        else
            em.merge(board);
        return board;
    }

    @Override
    public Optional<Board> findById(Long id) {
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }

    @Override
    public void deleteBoardById(Long id) {
        Board board = em.find(Board.class, id);
        board.delete();
    }
}
