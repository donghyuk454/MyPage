package com.mong.project.util.transformer;

import com.mong.project.domain.board.Board;
import com.mong.project.dto.request.board.CreateBoardRequest;

public class BoardTransformer {

    private BoardTransformer(){}

    public static Board createBoardRequestToBoard(CreateBoardRequest createBoardRequest) {
        return Board.builder()
                .title(createBoardRequest.getTitle())
                .content(createBoardRequest.getContent())
                .build();
    }
}
