package com.mong.project.dto.response.board;

import com.mong.project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private Long writerId;
    private String writerAlias;
    private LocalDateTime createDateTime;

    private List<GetBoardImageResponse> images;
    private List<GetBoardCommentResponse> comments;

    public GetBoardResponse(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerId = board.getMember().getId();
        this.writerAlias = board.getMember().getAlias();
        this.createDateTime = board.getCreatedDateTime();

        images = board.getImages().stream().map(i->
            new GetBoardImageResponse(i.getId(),i.getUrl())
        ).collect(Collectors.toList());

        comments = board.getComments().stream().map(c->
                new GetBoardCommentResponse(c.getId(), c.getMember().getId(), c.getMember().getAlias(), c.getContent())
        ).collect(Collectors.toList());
    }
}
