package com.mong.MyProject.domain.board;

import com.mong.MyProject.domain.BaseEntity;
import com.mong.MyProject.domain.image.board.BoardImage;
import com.mong.MyProject.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id", updatable = false)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(name="status")
    private BoardStatus status;

    @ManyToOne
    @JoinColumn(name="board")
    private Member member;

    @OneToMany(mappedBy = "image_id")
    private List<BoardImage> images = new ArrayList<>();

    public void setTitle(String title){
        this.title = title;
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content;
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    public void delete() {
        this.status = BoardStatus.DELETED;
        this.lastModifiedDateTime = LocalDateTime.now();
        this.deletedDateTime = lastModifiedDateTime;
    }
}
