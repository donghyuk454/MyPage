package com.mong.project.domain.board;

import com.mong.project.domain.BaseEntity;
import com.mong.project.domain.comment.Comment;
import com.mong.project.domain.image.Image;
import com.mong.project.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id @GeneratedValue
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, Member member){
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        if(member != null)
            member.addBoard(this);
        this.status = BoardStatus.ACTIVE;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.status = BoardStatus.DELETED;
        this.deletedDateTime = LocalDateTime.now();
        this.member.deleteBoard(this);
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
