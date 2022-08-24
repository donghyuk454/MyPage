package com.mong.project.util.transformer;

import com.mong.project.domain.member.Member;
import com.mong.project.dto.request.member.MemberJoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTransformerTest {

    @Test
    @DisplayName("MemberJoinRequest DTO 를 Member 로 변환합니다.")
    void joinRequestToMember() {
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("name")
                .alias("alias")
                .email("email@email.com")
                .passwd("passwd")
                .build();

        Member result = MemberTransformer.joinRequestToMember(memberJoinRequest);

        assertThat(result.getAlias()).isEqualTo(memberJoinRequest.getAlias());
        assertThat(result.getEmail()).isEqualTo(memberJoinRequest.getEmail());
        assertThat(result.getPasswd()).isEqualTo(memberJoinRequest.getPasswd());
        assertThat(result.getName()).isEqualTo(memberJoinRequest.getName());
    }
}