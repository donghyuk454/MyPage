package com.mong.project.controller.board.dto.request;

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
