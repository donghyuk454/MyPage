package com.mong.project.service.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.comment.CommentRepository;
import com.mong.project.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }
    /**
     * 댓글 생성
     * */
    public void addComment(Long memberId, Long boardId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD));

        commentRepository.save(member, board, content);
    }

    /**
     * 댓글 내용 수정
     * */
    public Comment changeComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_COMMENT));
        comment.setContent(content);

        return comment;
    }

    /**
     * 댓글 삭제
     * */
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
