package com.mong.MyProject.repository.comment;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.comment.Comment;
import com.mong.MyProject.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private EntityManager em;

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
        board.getComments().add(comment);
        em.persist(member);
        em.persist(comment);
        log.info("Comment 생성 : {}", comment.toString());
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Comment comment = em.find(Comment.class, id);
        log.info("Comment 찾기 : {}", comment.toString());
        return Optional.ofNullable(comment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = em.find(Comment.class, id);
        comment.getBoard().getComments().remove(comment);
        em.remove(comment);
    }
}
