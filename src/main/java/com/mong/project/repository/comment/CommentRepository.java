package com.mong.project.repository.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Member member, Board board, String content);
    Optional<Comment> findById(Long id);
    void deleteById(Long id);
}
