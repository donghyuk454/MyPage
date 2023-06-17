package com.mong.project.service.member;

import com.mong.project.domain.image.Image;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;

import org.junit.jupiter.api.*;
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

    private static Member memberInstance;

    @BeforeAll
    static void beforeAll() {
        memberInstance = Member.builder()
                .id(1L)
                .name("test")
                .email("email@email.com")
                .alias("string")
                .passwd("string")
                .build();
    }

    @Test
    @DisplayName("회원 가입")
    void join() {
        setMemberId();
        setMemberName();
        setMemberEmail();
        setMemberAlias();
        setValidSave(member);

        Long id = memberService.join(member);

        assertThat(id).isEqualTo(member.getId());

        verify(memberRepository,times(1))
                .save(any(Member.class));
        verify(memberRepository, times(1))
                .findByEmailAndAlias(anyString(), anyString());
    }

    @Test
    @DisplayName("기존에 존재하는 닉네임을 가진 회원 등록. IllegalException 을 throw 합니다")
    void joinExistAlias() {
        setMemberEmail();
        setMemberAlias();
        when(memberRepository.save(member))
                .thenThrow(new IllegalStateException("이미 존재하는 닉네임입니다."));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");

        verify(memberRepository, times(1))
                .findByEmailAndAlias(anyString(), anyString());
    }

    @Test
    @DisplayName("기존에 존재하는 이메일을 가진 회원 등록. IllegalException 을 throw 합니다")
    void joinExistEmail() {
        setMemberEmail();
        setMemberAlias();
        when(memberRepository.save(member))
                .thenThrow(new IllegalStateException(ErrorCode.ALREADY_EXIST_MEMBER));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);

        verify(memberRepository, times(1))
                .findByEmailAndAlias(anyString(), anyString());
    }

    @Test
    @DisplayName("로그인 기능입니다. 조회한 Member 를 return 합니다")
    void login() {
        setMemberId();
        setMemberEmail();
        setMemberPassword();
        setValidFindByEmail();

        Long member_id = memberService.login(member.getEmail(), member.getPasswd());

        assertThat(member_id).isEqualTo(member.getId());
        verify(memberRepository, times(1))
                .findByEmail(anyString());
        verify(member, times(2))
                .getPasswd();
    }

    private void setValidFindByEmail() {
        when(memberRepository.findByEmail(member.getEmail()))
                .thenReturn(Optional.of(member));
    }

    private void setMemberPassword() {
        when(member.getPasswd())
                .thenReturn(memberInstance.getPasswd());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 하는 경우입니다. NoSuchElementException 을 throw 합니다.")
    void loginInvalidEmail() {
        when(memberRepository.findByEmail("none"))
                .thenReturn(Optional.empty());

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> memberService.login("none", "none"));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.INVALID_EMAIL);
        verify(memberRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    @DisplayName("패스워드가 다른 경우입니다. IllegalStateException 을 throw 합니다.")
    void loginInvalidPassword() {
        setMemberEmail();
        setMemberPassword();
        when(memberRepository.findByEmail(member.getEmail()))
                .thenReturn(Optional.of(member));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.login(member.getEmail(), "none"));

        assertThat(e.getMessage()).isEqualTo(ErrorCode.INVALID_PASSWORD);
        verify(memberRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    @DisplayName("비밀번호를 변경합니다.")
    void changePassword() {
        setMemberId();
        setValidFindById();
        when(member.getPasswd())
                .thenReturn("newPassword");

        String newPasswd = "newPassword";
        memberService.changePasswd(member.getId(), newPasswd);

        assertThat(member.getPasswd()).isEqualTo(newPasswd);
        verify(memberRepository, times(1))
                .findById(any(Long.class));
        verify(member, times(1))
                .setPasswd(anyString());
    }

    @Test
    @DisplayName("맴버의 image 를 추가하거나 변경합니다.")
    void setMemberImage() {
        setValidFindById();
        setValidSave(member);
        File imageFile = mock(File.class);
        when(fileService.convertToFile(any()))
                .thenReturn(imageFile);
        when(imageFile.getAbsolutePath())
                .thenReturn("/this/is/absolute/path.PNG");
        MultipartFile image = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());

        memberService.setImage(1L, image);

        verify(member).setImage(any(Image.class));
    }

    private void setValidSave(Member member) {
        when(memberRepository.save(member))
                .thenReturn(member);
    }

    private void setValidFindById() {
        when(memberRepository.findById(memberInstance.getId()))
                .thenReturn(Optional.of(member));
    }

    @Test
    @DisplayName("맴버의 이미지를 삭제합니다.")
    void deleteMemberImage(){
        setValidFindById();
        setValidSave(member);
        Image image = mock(Image.class);
        when(member.getImage())
                .thenReturn(image);
        when(image.getUrl())
                .thenReturn("/this/is/absolute/path.PNG");
        when(fileService.removeFileByPath(anyString()))
                .thenReturn(true);

        memberService.deleteImage(1L);

        verify(member).setImage(null);
    }

    private void setMemberEmail() {
        when(member.getEmail())
                .thenReturn(memberInstance.getEmail());
    }

    private void setMemberName() {
        when(member.getName())
                .thenReturn(memberInstance.getName());
    }

    private void setMemberId() {
        when(member.getId())
                .thenReturn(memberInstance.getId());
    }

    private void setMemberAlias() {
        when(member.getAlias())
                .thenReturn(memberInstance.getAlias());
    }
}