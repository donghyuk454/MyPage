package com.mong.project.dto.request.member;


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
}
