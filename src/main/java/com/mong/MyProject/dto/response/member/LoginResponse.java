package com.mong.MyProject.dto.response.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private Long member_id;

    public LoginResponse(Long member_id){
        this.member_id = member_id;
    }
}
