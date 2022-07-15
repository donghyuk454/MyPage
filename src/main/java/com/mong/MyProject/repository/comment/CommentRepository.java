package com.mong.MyProject.repository.comment;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.comment.Comment;
import com.mong.MyProject.domain.member.Member;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Member member, Board board, String content);
    Optional<Comment> findById(Long id);
    void deleteById(Long id);
}
