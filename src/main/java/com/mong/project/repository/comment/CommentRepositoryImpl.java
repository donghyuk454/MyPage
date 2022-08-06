package com.mong.project.repository.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private final EntityManager em;

    @Autowired
    public CommentRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Comment save(Member member, Board board, String content) {
        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .content(content)
                .build();
        em.persist(comment);
        log.info("Comment 생성 : {}", comment.toString());
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Comment comment = em.find(Comment.class, id);
        log.info("Comment 찾기 : {}", comment.toString());
        return Optional.of(comment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = em.find(Comment.class, id);
        Board board = comment.getBoard();
        board.getComments().remove(comment);
        em.remove(comment);
    }
}
