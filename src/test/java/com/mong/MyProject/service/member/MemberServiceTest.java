package com.mong.MyProject.service.member;

import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.member.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("회원 가입")
    void 회원_가입() {
        member = Member.builder()
                .name("test")
                .email("email@email.com")
                .alias("string")
                .passwd("string").build();

        when(memberRepository.save(member))
                .then(m->{
                    Member temp = Member.builder()
                            .id(1L)
                            .name("test")
                            .email("email@email.com")
                            .alias("string")
                            .passwd("string").build();
                    return temp;
                });

        Long id = memberService.join(member);

        assertThat(id).isEqualTo(member.getId());

        verify(memberRepository,times(1))
                .save(any(Member.class));
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
        verify(memberRepository, times(1))
                .findByAlias(any(String.class));
    }

    @Test
    @DisplayName("기존에 존재하는 닉네임을 가진 회원 등록. IllegalException 을 throw 합니다")
    void 회원_가입_존재하는_닉네임() {
        memberService.join(member);
        Member member1 = mock(Member.class);

        when(memberRepository.save(member1))
                .thenThrow(new IllegalStateException("이미 존재하는 닉네임입니다."));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member1));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");

        verify(memberRepository, times(2))
                .findByEmail(any());
        verify(memberRepository, times(2))
                .findByAlias(any());
    }

    @Test
    @DisplayName("기존에 존재하는 이메일을 가진 회원 등록. IllegalException 을 throw 합니다")
    void 회원_가입_존재하는_이메일() {
        memberService.join(member);
        Member member1 = mock(Member.class);

        when(memberRepository.save(member1))
                .thenThrow(new IllegalStateException("이미 존재하는 회원입니다."));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member1));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        verify(memberRepository, times(2))
                .findByEmail(any());
    }

    @Test
    @DisplayName("로그인 기능입니다. 조회한 Member 를 return 합니다")
    void 로그인() {
        when(member.getPasswd())
                .thenReturn("string");
        when(member.getEmail())
                .thenReturn("email@email.com");

        when(memberRepository.findByEmail("email@email.com"))
                .thenReturn(Optional.ofNullable(member));

        Member result = memberService.login(member.getEmail(), member.getPasswd());
        assertThat(result).isEqualTo(member);
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
        verify(member, times(2))
                .getPasswd();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 하는 경우입니다. NoSuchElementException 을 throw 합니다.")
    void 로그인_아이디_없음() {
        when(memberRepository.findByEmail("none"))
                .thenReturn(Optional.empty());

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> memberService.login("none", "none"));

        assertThat(e.getMessage()).isEqualTo("존재하지 않는 이메일 입니다.");
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
    }

    @Test
    @DisplayName("패스워드가 다른 경우입니다. IllegalStateException 을 throw 합니다.")
    void 로그인_패스워드_오류() {
        when(member.getPasswd())
                .thenReturn("string");

        when(memberRepository.findByEmail("email@email.com"))
                .thenReturn(Optional.ofNullable(member));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.login("email@email.com", "none"));

        assertThat(e.getMessage()).isEqualTo("비밀번호가 다릅니다.");
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
    }

    @Test
    @DisplayName("비밀번호를 변경합니다.")
    void 비밀번호_변경() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.ofNullable(member));
        when(member.getPasswd())
                .thenReturn("newPassword");

        String newPasswd = "newPassword";
        memberService.changePasswd(1L, newPasswd);

        assertThat(member.getPasswd()).isEqualTo(newPasswd);
        verify(memberRepository, times(1))
                .findById(any(Long.class));
        verify(member, times(1))
                .setPasswd(any(String.class));
    }
}