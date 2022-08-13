package com.mong.project.util.transformer;

import com.mong.project.domain.board.Board;
import com.mong.project.dto.request.board.CreateBoardRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTransformerTest {

    @Test
    @DisplayName("CreateBoardRequest DTO 의 board 관련된 정보를 board 로 변환합니다.")
    void createBoardRequestToBoard() {
        CreateBoardRequest createBoardRequest
                = new CreateBoardRequest(1L, "title", "content");

        Board board = BoardTransformer.createBoardRequestToBoard(createBoardRequest);

        assertThat(board.getTitle()).isEqualTo(createBoardRequest.getTitle());
        assertThat(board.getContent()).isEqualTo(createBoardRequest.getContent());
    }
}