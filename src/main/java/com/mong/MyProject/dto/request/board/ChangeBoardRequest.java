package com.mong.MyProject.dto.request.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBoardRequest {
    private Long board_id;
    private String title;
    private String content;
}
