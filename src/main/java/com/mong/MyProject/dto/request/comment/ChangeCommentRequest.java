package com.mong.MyProject.dto.request.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCommentRequest {
    private Long comment_id;
    private String content;
}
