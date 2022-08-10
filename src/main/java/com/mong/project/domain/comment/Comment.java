package com.mong.project.domain.comment;

import com.mong.project.domain.BaseEntity;
import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="comment")
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @Column(name = "content")
    private String content;

    @Builder
    public Comment(Long id, Member member, Board board, String content) {
        this.id = id;
        this.member = member;
        if (member != null)
            board.getComments().add(this);
        this.board = board;
        this.content = content;
        this.createdDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = this.createdDateTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "createdDateTime=" + createdDateTime +
                ", lastModifiedDateTime=" + lastModifiedDateTime +
                ", deletedDateTime=" + deletedDateTime +
                ", id=" + id +
                ", member=" + member +
                ", board=" + board +
                ", content='" + content + '\'' +
                '}';
    }

    public void setContent(String content) {
        this.content = content;
        this.lastModifiedDateTime = LocalDateTime.now();
    }
}
