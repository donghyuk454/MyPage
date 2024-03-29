package com.mong.project.service.comment;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.MyPageException;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.comment.CommentRepository;
import com.mong.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mong.project.exception.ErrorCode.*;

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
                .orElseThrow(() -> new MyPageException(NOT_EXIST_MEMBER));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MyPageException(NOT_EXIST_BOARD));

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
                .orElseThrow(() -> new MyPageException(NOT_EXIST_COMMENT));
        comment.setContent(content);

        return comment;
    }

    /**
     * 댓글 삭제
     * */
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MyPageException(NOT_EXIST_COMMENT));

        Board board = comment.getBoard();
        List<Comment> comments = board.getComments();
        comments.remove(comment);
        comment.delete();

        commentRepository.save(comment);
        boardRepository.save(board);
    }
}
