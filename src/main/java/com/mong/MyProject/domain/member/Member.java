package com.mong.MyProject.domain.member;

import com.mong.MyProject.domain.BaseEntity;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.image.user.MemberImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @OneToOne(mappedBy = "member")
    private MemberImage memberImage;

    @OneToMany(mappedBy = "member")
    private List<Board> boards;

    @Builder
    public Member(Long id, String name, String email, String alias, String passwd, MemberImage memberImage, List<Board> boards) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.alias = alias;
        this.passwd = passwd;
        this.memberImage = memberImage;
        this.boards = boards;
        this.createdDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = createdDateTime;
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
}
