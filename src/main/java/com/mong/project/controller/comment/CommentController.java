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

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@RequestBody AddCommentRequest addCommentRequest) {
        commentService.addComment(addCommentRequest.getMember_id(), addCommentRequest.getBoard_id(), addCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<Void> changeComment(@RequestBody ChangeCommentRequest changeCommentRequest){
        commentService.changeComment(changeCommentRequest.getComment_id(), changeCommentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("comment")
    public ResponseEntity<Void> deleteComment(@RequestParam(name = "comment_id") Long comment_id) {
        commentService.deleteComment(comment_id);
        return ResponseEntity.ok().build();
    }
}
