package com.mong.MyProject.service.member;

import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceTest(MemberService memberService, MemberRepository memberRepository){
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Test
    void 회원_가입() {
        Member member = new Member("name", "email@email.com", "alias", "password", LocalDateTime.now());
        Long id = memberService.join(member);

        assertThat(member.getId()).isEqualTo(id);
    }

    @Test
    void 로그인() {
        Member member = new Member("name", "email@email.com", "alias", "password", LocalDateTime.now());
        memberService.join(member);

        Member result = memberService.login(member.getEmail(), member.getPasswd());

        assertThat(member).isEqualTo(result);
    }

    @Test
    void 로그인_아이디_없음() {
    }

    @Test
    void 로그인_패스워드_오류() {
    }

    @Test
    void 비밀번호_변경() {
        Member member = new Member("name", "email@email.com", "alias", "password", LocalDateTime.now());
        Long id = memberService.join(member);

        String newPasswd = "newPassword";
        memberService.changePasswd(id, newPasswd);

        assertThat(member.getPasswd()).isEqualTo(newPasswd);
    }
}