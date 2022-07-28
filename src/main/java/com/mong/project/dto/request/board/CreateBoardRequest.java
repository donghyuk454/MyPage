package com.mong.project.dto.request.board;

import com.mong.project.domain.board.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequest {
    private String title;
    private String content;

    public Board toBoard() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}
