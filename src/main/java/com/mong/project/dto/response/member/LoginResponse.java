package com.mong.project.dto.response.member;

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
