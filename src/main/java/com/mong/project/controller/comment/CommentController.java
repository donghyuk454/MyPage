package com.mong.project.controller.comment;

import com.mong.project.dto.request.comment.AddCommentRequest;
import com.mong.project.dto.request.comment.ChangeCommentRequest;
import com.mong.project.service.comment.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> addComment(@RequestBody AddCommentRequest addCommentRequest) {
        commentService.addComment(addCommentRequest.getMemberId(), addCommentRequest.getBoardId(), addCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comments")
    public ResponseEntity<Void> changeComment(@RequestBody ChangeCommentRequest changeCommentRequest){
        commentService.changeComment(changeCommentRequest.getCommentId(), changeCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("comments")
    public ResponseEntity<Void> deleteComment(@RequestParam(name = "comment_id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
