package com.mong.project.controller.member.dto.response;

import com.mong.project.domain.member.Member;
import lombok.Data;

@Data
public class GetMemberResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String alias;

    public GetMemberResponse (Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.alias = member.getAlias();
    }
}
