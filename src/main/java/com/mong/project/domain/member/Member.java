package com.mong.project.domain.member;

import com.mong.project.domain.BaseEntity;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity(name="member")
@Getter
@Setter
@Table(name="member", uniqueConstraints = {
        @UniqueConstraint(name = "user_unique_constraint", columnNames = {"email", "alias"})
})
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="member_id", updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "alias")
    private String alias;

    @Column(name = "passwd")
    private String passwd;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Board> boards;

    @Builder
    public Member(Long id, String name, String email, String alias, String passwd, Image image, List<Board> boards) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.alias = alias;
        this.passwd = passwd;
        this.image = image;
        this.boards = boards;
        this.createdDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = createdDateTime;
        this.boards = new ArrayList<Board>();
    }

    @Override
    public String toString() {
        return "Member{" +
                "createdDateTime=" + createdDateTime +
                ", lastModifiedDateTime=" + lastModifiedDateTime +
                ", deletedDateTime=" + deletedDateTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", alias='" + alias + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    public void addBoard(Board board) {
        //무한 루프 방지
        if(!boards.contains(board))
            this.boards.add(board);

        if (board.getMember() == null)
            board.setMember(this);
    }

    public void deleteBoard(Board board) {
        //무한 루프 방지
        if(boards.contains(board))
            boards.remove(board);
    }

    public void delete(){
        this.deletedDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = this.deletedDateTime;
    }
}
