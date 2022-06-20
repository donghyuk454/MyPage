package com.mong.MyProject.domain.user;

import com.mong.MyProject.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity(name="user")
@Getter
@Setter
@Table(name="user", uniqueConstraints = {
        @UniqueConstraint(name = "user_unique_constraint", columnNames = {"email", "alias"})
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false)
    private Long id;

    private String name;
    private String email;
    private String alias;
    private String passwd;




}
