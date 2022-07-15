package com.mong.MyProject.service.comment;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.comment.Comment;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.comment.CommentRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private MemberRepository memberRepository;
    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }
    /**
     * 댓글 생성
     * */
    public Comment addComment(Long member_id, Long board_id, String content) {
        Member member = memberRepository.findById(member_id).get();
        Board board = boardRepository.findById(board_id).get();

        return commentRepository.save(member, board, content);
    }

    /**
     * 댓글 내용 수정
     * */
    public Comment changeComment(Long comment_id, String content) {
        Comment comment = commentRepository.findById(comment_id).get();
        comment.setContent(content);

        return comment;
    }

    /**
     * 댓글 삭제
     * */
    public void deleteComment(Long comment_id) {
        commentRepository.deleteById(comment_id);
    }
}
