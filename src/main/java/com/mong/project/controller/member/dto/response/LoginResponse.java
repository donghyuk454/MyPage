package com.mong.project.controller.member.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private Long memberId;

    public LoginResponse(Long memberId){
        this.memberId = memberId;
    }
}
