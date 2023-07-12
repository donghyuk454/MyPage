package com.mong.project.service.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.comment.CommentRepository;
import com.mong.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 생성
     * */
    public void addComment(Long memberId, Long boardId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD));

        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .content(content)
                .build();

        commentRepository.save(comment);
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
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.NOT_EXIST_COMMENT));

        List<Comment> comments = comment.getBoard().getComments();
        comments.remove(comment);

        commentRepository.deleteById(commentId);
    }
}
