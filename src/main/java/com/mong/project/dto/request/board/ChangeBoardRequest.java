package com.mong.project.dto.request.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBoardRequest {
    private Long boardId;
    private String title;
    private String content;
}
