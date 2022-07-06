package com.mong.MyProject.repository.member;

import com.mong.MyProject.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    private MemberRepositoryImpl memberRepository;

    @Test
    void save() {
        Member member = newTestMember();

        Member result = memberRepository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    void findByEmailAndPasswd() {
        Member member = newTestMember();

        Member result = memberRepository.findByEmailAndPasswd(member.getEmail(), member.getPasswd()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    void findByAlias() {
        Member member = newTestMember();

        Member result = memberRepository.findByAlias(member.getAlias()).get();
        assertThat(result).isEqualTo(member);
    }

    private Member newTestMember(){
        Member member = new Member("name", "tt@test.com", "alias", "passwd", LocalDateTime.now());
        memberRepository.save(member);
        return member;
    }
}