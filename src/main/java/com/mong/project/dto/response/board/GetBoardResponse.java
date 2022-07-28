package com.mong.project.dto.response.board;

import com.mong.project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private Long writerId;
    private String writerAlias;
    private LocalDateTime createDateTime;

    private List<GetBoardImageResponse> images = new ArrayList<>();
    private List<GetBoardCommentResponse> comments = new ArrayList<>();

    public GetBoardResponse(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerId = board.getMember().getId();
        this.writerAlias = board.getMember().getAlias();
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
