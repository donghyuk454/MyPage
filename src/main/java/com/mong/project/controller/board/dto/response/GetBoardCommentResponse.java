package com.mong.project.controller.board.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetBoardCommentResponse {
    private Long commentId;
    private Long writerId;
    private String writerAlias;
    private String content;
}
