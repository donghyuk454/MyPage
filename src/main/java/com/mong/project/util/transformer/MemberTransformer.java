package com.mong.project.util.transformer;

import com.mong.project.domain.member.Member;
import com.mong.project.dto.request.member.MemberJoinRequest;

public class MemberTransformer {

    private MemberTransformer(){}

    public static Member joinRequestToMember(MemberJoinRequest memberJoinRequest) {
        return Member.builder()
                .name(memberJoinRequest.getName())
                .email(memberJoinRequest.getEmail())
                .alias(memberJoinRequest.getAlias())
                .passwd(memberJoinRequest.getPasswd())
                .build();
    }
}