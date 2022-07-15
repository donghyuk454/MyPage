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

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoardImage> images = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, Member member){
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        this.status = BoardStatus.ACTIVE;
        this.createdDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = this.createdDateTime;
    }

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
        this.deletedDateTime = LocalDateTime.now();
        this.member.deleteBoard(this);
    }
}
