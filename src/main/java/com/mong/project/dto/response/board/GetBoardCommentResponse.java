package com.mong.project.dto.response.board;

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
