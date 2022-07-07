package com.mong.MyProject.domain.member;

import com.mong.MyProject.domain.BaseEntity;

import com.mong.MyProject.domain.article.Article;
import com.mong.MyProject.domain.image.user.MemberImage;
import lombok.Getter;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "alias")
    private String alias;
    @Column(name = "passwd")
    private String passwd;

    public Member(String name, String email, String alias, String passwd, LocalDateTime createdDateTime) {
        this.name = name;
        this.email = email;
        this.alias = alias;
        this.passwd = passwd;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = createdDateTime;
    }

    public void changed(){
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    

//    @OneToOne
//    private MemberImage memberImage;
//
//    @OneToMany
//    private List<Article> articles;
}
