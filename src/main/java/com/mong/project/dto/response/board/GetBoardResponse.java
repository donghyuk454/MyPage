package com.mong.project.dto.response.board;

import com.mong.project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBoardResponse {
    private Long board_id;
    private String title;
    private String content;
    private Long writer_id;
    private String writer_alias;
    private LocalDateTime createDateTime;

    private List<GetBoardImageResponse> images = new ArrayList<>();
    private List<GetBoardCommentResponse> comments = new ArrayList<>();

    public GetBoardResponse(Board board) {
        this.board_id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer_id = board.getMember().getId();
        this.writer_alias = board.getMember().getAlias();
        this.createDateTime = board.getCreatedDateTime();
        board.getImages().forEach(image -> {
            GetBoardImageResponse temp = new GetBoardImageResponse(image.getId(), image.getUrl());
            this.images.add(temp);
        });
        board.getComments().forEach(comment -> {
            GetBoardCommentResponse temp = new GetBoardCommentResponse(comment.getId(), comment.getMember().getId(), comment.getMember().getAlias(), comment.getContent());
        });
    }
}
