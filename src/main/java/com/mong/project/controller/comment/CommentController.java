package com.mong.project.controller.comment;

import com.mong.project.dto.request.comment.AddCommentRequest;
import com.mong.project.dto.request.comment.ChangeCommentRequest;
import com.mong.project.service.comment.CommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody AddCommentRequest addCommentRequest) {
        commentService.addComment(addCommentRequest.getMemberId(), addCommentRequest.getBoardId(), addCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> changeComment(@RequestBody ChangeCommentRequest changeCommentRequest){
        commentService.changeComment(changeCommentRequest.getCommentId(), changeCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@RequestParam(name = "comment_id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
