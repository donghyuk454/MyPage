package com.mong.project.controller.member.dto.request;


import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.mong.project.exception.ErrorCode.*;
import static com.mong.project.exception.ErrorCode.INVALID_PASSWORD_FORMAT;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;
    @NotBlank
    private String alias;
    @NotBlank
    @Size(min = 8, max = 15, message = INVALID_PASSWORD_FORMAT)
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
