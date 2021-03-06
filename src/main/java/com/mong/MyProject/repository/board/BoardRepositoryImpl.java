package com.mong.MyProject.repository.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class BoardRepositoryImpl implements BoardRepository{

    private EntityManager em;

    @Autowired
    public BoardRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Board save(Member member, Board board) {
        board.setMember(member);
        em.persist(member);
        return board;
    }

    @Override
    public Board save(Board board) {
        em.persist(board);
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
