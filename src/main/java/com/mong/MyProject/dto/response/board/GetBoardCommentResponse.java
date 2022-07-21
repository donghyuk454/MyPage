package com.mong.MyProject.dto.response.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetBoardCommentResponse {
    private Long comment_id;
    private Long writer_id;
    private String writer_alias;
    private String content;
}
