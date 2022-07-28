package com.mong.project.service.member;

import com.mong.project.domain.image.Image;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private FileService fileService;

    @Mock
    private Member member;

    @Test
    @DisplayName("회원 가입")
    void join() {
        member = Member.builder()
                .name("test")
                .email("email@email.com")
                .alias("string")
                .passwd("string").build();

        when(memberRepository.save(member))
                .thenReturn(Member.builder()
                        .id(1L)
                        .name("test")
                        .email("email@email.com")
                        .alias("string")
                        .passwd("string").build());

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
    void joinExistAlias() {
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
    void joinExistEmail() {
        memberService.join(member);
        Member member1 = mock(Member.class);

        when(memberRepository.save(member1))
                .thenThrow(new IllegalStateException(ErrorCode.ALREADY_EXIST_MEMBER));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member1));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);

        verify(memberRepository, times(2))
                .findByEmail(any());
    }

    @Test
    @DisplayName("로그인 기능입니다. 조회한 Member 를 return 합니다")
    void login() {
        when(member.getPasswd())
                .thenReturn("string");
        when(member.getEmail())
                .thenReturn("email@email.com");
        when(member.getId())
                .thenReturn(1L);

        when(memberRepository.findByEmail("email@email.com"))
                .thenReturn(Optional.of(member));

        Long member_id = memberService.login(member.getEmail(), member.getPasswd());

        assertThat(member_id).isEqualTo(member.getId());
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
        verify(member, times(2))
                .getPasswd();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 하는 경우입니다. NoSuchElementException 을 throw 합니다.")
    void loginInvalidEmail() {
        when(memberRepository.findByEmail("none"))
                .thenReturn(Optional.empty());

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> memberService.login("none", "none"));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.INVALID_EMAIL);
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
    }

    @Test
    @DisplayName("패스워드가 다른 경우입니다. IllegalStateException 을 throw 합니다.")
    void loginInvalidPassword() {
        when(member.getPasswd())
                .thenReturn("string");

        when(memberRepository.findByEmail("email@email.com"))
                .thenReturn(Optional.of(member));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.login("email@email.com", "none"));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.INVALID_PASSWORD);
        verify(memberRepository, times(1))
                .findByEmail(any(String.class));
    }

    @Test
    @DisplayName("비밀번호를 변경합니다.")
    void changePassword() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));
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

    @Test
    @DisplayName("맴버의 image 를 추가하거나 변경합니다.")
    void setMemberImage(){
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));
        when(memberRepository.save(member))
                .thenReturn(member);
        File imageFile = mock(File.class);
        when(fileService.convertToFile(any()))
                .thenReturn(imageFile);
        when(imageFile.getAbsolutePath())
                .thenReturn("/this/is/absolute/path.PNG");
        MultipartFile image = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());

        memberService.setImage(1L, image);

        verify(member).setImage(any(Image.class));
    }

    @Test
    @DisplayName("맴버의 이미지를 삭제합니다.")
    void deleteMemberImage(){
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));
        when(memberRepository.save(member))
                .thenReturn(member);
        Image image = mock(Image.class);
        when(member.getImage())
                .thenReturn(image);
        when(image.getUrl())
                .thenReturn("/this/is/absolute/path.PNG");
        when(fileService.removeFileByPath(any(String.class)))
                .thenReturn(true);

        memberService.deleteImage(1L);

        verify(member).setImage(null);
    }
}