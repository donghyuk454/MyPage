package com.mong.MyProject.service.member;

import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {


    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     * */
    public Long join(Member member) {
        validateDuplicateMember(member);
        validateDuplicateAlias(member);

        memberRepository.save(member);
        log.info("회원 가입 id = {}, name = {}, alias = {}, email = {}", member.getId(), member.getName(), member.getAlias(), member.getEmail());
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void validateDuplicateAlias(Member member) {
        memberRepository.findByAlias(member.getAlias())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
    }

    /**
     * 로그인
     * */
    public Member login(String email, String passwd) {
        return memberRepository.findByEmailAndPasswd(email, passwd).get();
    }

    /**
     * 비밀번호 변경
     * */

    public void changePasswd(Long id, String passwd) {
        Member member = memberRepository.findById(id).get();

        member.setPasswd(passwd);
        memberRepository.save(member);
    }
}
