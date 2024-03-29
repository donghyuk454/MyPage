package com.mong.project.controller.comment;

import com.mong.project.domain.member.annotation.Login;
import com.mong.project.controller.comment.dto.request.AddCommentRequest;
import com.mong.project.controller.comment.dto.request.ChangeCommentRequest;
import com.mong.project.service.comment.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v2/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@Login final Long memberId,
                                           @Valid @RequestBody AddCommentRequest addCommentRequest) {
        commentService.addComment(memberId, addCommentRequest.getBoardId(), addCommentRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> changeComment(@Login final Long memberId,
                                              @Valid @RequestBody ChangeCommentRequest changeCommentRequest){
        commentService.changeComment(changeCommentRequest.getCommentId(), changeCommentRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@Login final Long memberId,
                                              @NotNull @RequestParam(name = "commentId") Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }
}
