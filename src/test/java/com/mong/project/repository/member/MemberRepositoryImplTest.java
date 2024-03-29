package com.mong.project.repository.member;

import com.mong.project.domain.member.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 member 에 영속성을 부여합니다")
    void createMember() {
        //given
        Member member = newTestMember();

        //when
        Member result = memberRepository.findById(member.getId()).get();

        //then
        assertThat(member).isEqualTo(result);
        assertNotNull(member);
    }

    @Test
    @DisplayName("email 을 통해 member 를 조회합니다")
    void getMemberByEmail(){
        //given
        Member member = newTestMember();
        assertNotNull(member);

        //when
        Member result = memberRepository.findByEmail(member.getEmail()).get();

        //then
        assertNotNull(result);
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("alias 를 통해 member 를 조회합니다")
    void getMemberByAlias() {
        //given
        Member member = newTestMember();
        //when
        Member result = memberRepository.findByAlias(member.getAlias()).get();
        //then
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("모든 member 를 조회합니다")
    void getAllMember(){
        //given
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Member member = Member.builder().name("name"+i)
                    .email(i+"tt@test.com")
                    .alias("테스트 닉네임"+i)
                    .passwd("passwd")
                    .build();
            members.add(memberRepository.save(member));
        }

        //when
        List<Member> result = memberRepository.findAll();

        //then
        assertThat(result).isEqualTo(members);
    }

    private Member newTestMember(){
        return memberRepository.save(Member.builder()
                .name("name").email("tt@test.com").alias("테스트 닉네임").passwd("passwd").build());
    }
}