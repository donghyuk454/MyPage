package com.mong.project.dto.request.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {
    private Long boardId;
    private String content;
}
