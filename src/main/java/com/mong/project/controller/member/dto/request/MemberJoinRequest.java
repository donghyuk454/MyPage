package com.mong.project.controller.member.dto.request;


import com.mong.project.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {
    private String name;

    private String email;

    private String alias;

    private String passwd;

    @Builder
    public MemberJoinRequest(String name, String email, String alias, String passwd) {
        this.name = name;
        this.email = email;
        this.alias = alias;
        this.passwd = passwd;
    }

    public Member toMember() {
        return Member.builder()
                .name(name)
                .email(email)
                .alias(alias)
                .passwd(passwd)
                .build();
    }
}
