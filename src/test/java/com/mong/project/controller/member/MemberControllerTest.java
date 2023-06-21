package com.mong.project.controller.member;

import com.mong.project.controller.AbstractControllerTest;
import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import com.mong.project.controller.member.dto.request.ChangePasswordRequest;
import com.mong.project.controller.member.dto.request.LoginRequest;
import com.mong.project.controller.member.dto.request.MemberJoinRequest;
import com.mong.project.exception.ErrorCode;
import com.mong.project.service.member.MemberService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends AbstractControllerTest {

    private static final String BASE_MEMBER_URI = "/api/v2/members";
    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Override
    protected Object setController() {
        return memberController;
    }


    @Test
    @DisplayName("회원 가입을 합니다. 성공 시 200 으로 응답합니다.")
    void join() throws Exception {
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(memberJoinRequest, BASE_MEMBER_URI + "/signup");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입을 하는 경우, 중복된 alias 인 경우 이미 존재하는 닉네임으로 인식하여, IllegalStateException 이 발생하고 400 을 응답합니다.")
    void joinDuplicateAlias() throws Exception {
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(memberJoinRequest, BASE_MEMBER_URI + "/signup");

        when(memberService.join(any()))
                .thenThrow(new IllegalStateException(ErrorCode.ALREADY_EXIST_ALIAS));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입을 하는 경우, 중복된 email 인 경우 이미 존재하는 회원으로 인식하여, IllegalStateException 이 발생하고 400 을 응답합니다.")
    void joinDuplicateEmail() throws Exception {
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(memberJoinRequest, BASE_MEMBER_URI + "/signup");

        when(memberService.join(any()))
                .thenThrow(new IllegalStateException(ErrorCode.ALREADY_EXIST_MEMBER));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    private static MemberJoinRequest createMemberJoinRequest() {
        return MemberJoinRequest.builder()
                .name("이름")
                .email("email@email.com")
                .passwd("password")
                .alias("별칭").build();
    }

    @Test
    @DisplayName("로그인 합니다. 성공 시 200 을 응답합니다.")
    void login() throws Exception {
        LoginRequest loginRequest = createLoginRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(loginRequest, BASE_MEMBER_URI + "/login");

        when(memberService.login("email", "password"))
                .thenReturn(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인을 실패할 경우 중 가입한 이메일이 아닌 경우 NoSuchElementException 가 발생하고 400 을 응답합니다.")
    void loginInvalidEmail() throws Exception {
        LoginRequest loginRequest = createLoginRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(loginRequest, BASE_MEMBER_URI + "/login");

        when(memberService.login("email", "password"))
                .thenThrow(new NoSuchElementException(ErrorCode.INVALID_EMAIL));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    private static LoginRequest createLoginRequest() {
        return new LoginRequest("email", "password");
    }

    @Test
    @DisplayName("로그인을 실패할 경우 중 비밀번호를 틀린 경우 NoSuchElementException 가 발생하고 400 을 응답합니다.")
    void loginInvalidPassword() throws Exception {
        LoginRequest loginRequest = createLoginRequest();

        MockHttpServletRequestBuilder builder = createPostMockHttpServletRequest(loginRequest, BASE_MEMBER_URI + "/login");

        when(memberService.login("email", "password"))
                .thenThrow(new IllegalStateException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원의 id 값을 통해 회원의 정보를 조회합니다. 성공 시 200 을 응답합니다.")
    void getMember() throws Exception {
        MockHttpServletRequestBuilder builder = get(BASE_MEMBER_URI);

        Member member = Member.builder()
                .id(1L)
                .name("name")
                .email("email.com")
                .alias("alias").build();

        when(memberService.getMemberById(1L))
                .thenReturn(member);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 회원의 id 값을 통해 회원의 정보를 조회합니다. NoSuchElementException 이 발생하고, 400 을 응답합니다.")
    void getNotExistMember() throws Exception {
        MockHttpServletRequestBuilder builder = get(BASE_MEMBER_URI);

        when(memberService.getMemberById(1L))
                .thenThrow(new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경합니다. 성공 시 200 을 응답합니다.")
    void changePassword() throws Exception {
        ChangePasswordRequest changePasswordRequest
                = new ChangePasswordRequest("newPasswd");

        MockHttpServletRequestBuilder builder
                = createPutMockHttpServletRequest(changePasswordRequest, BASE_MEMBER_URI + "/password");

        doNothing().when(memberService)
                .changePasswd(1L, changePasswordRequest.getNewPasswd());

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("member 가 작성한 boards 를 조회합니다. 성공 시 200 을 응답합니다.")
    void getMemberBoards() throws Exception {
        MockHttpServletRequestBuilder builder = get(BASE_MEMBER_URI + "/board");

        Board board1 = mock(Board.class);
        List<Board> boards = List.of(board1);

        Member member = mock(Member.class);
        when(memberService.getMemberById(1L))
                .thenReturn(member);
        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("테스트용");
        when(member.getBoards()).thenReturn(boards);

        when(board1.getMember()).thenReturn(member);
        when(board1.getContent()).thenReturn("content");
        when(board1.getTitle()).thenReturn("title");
        when(board1.getId()).thenReturn(1L);
        when(board1.getCreatedDateTime()).thenReturn(LocalDateTime.now());

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원의 이미지를 추가하거나 변경합니다. 성공 시 200 을 응답합니다.")
    void setMemberImage() throws Exception {
        MockMultipartFile file  // multipart file name 과 request param name 과 같아야 함
                = new MockMultipartFile("image", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        doNothing().when(memberService)
                .setImage(1L, file);

        MockHttpServletRequestBuilder builder = multipart(BASE_MEMBER_URI + "/image")
                .file(file)
                .param("memberId", "1");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원의 이미지를 삭제합니다. MultipartFile 이 없는 경우 사진이 삭제됩니다. 성공 시 200 을 응답합니다.")
    void deleteMemberImage() throws Exception {
        MockHttpServletRequestBuilder builder = delete(BASE_MEMBER_URI + "/image");

        doNothing().when(memberService)
                .deleteImage(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원을 삭제합니다. 성공 시 200 을 응답합니다.")
    void deleteMember() throws Exception {
        MockHttpServletRequestBuilder builder = delete(BASE_MEMBER_URI);

        doNothing().when(memberService)
                .deleteMember(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }
}