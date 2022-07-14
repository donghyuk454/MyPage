package com.mong.MyProject.domain.comment;

import com.mong.MyProject.domain.BaseEntity;
import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

}
