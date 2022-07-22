package com.mong.MyProject.controller.member;

import com.google.gson.Gson;
import com.mong.MyProject.controller.AbstractControllerTest;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.dto.request.member.ChangePasswordRequest;
import com.mong.MyProject.dto.request.member.LoginRequest;
import com.mong.MyProject.dto.request.member.MemberJoinRequest;
import com.mong.MyProject.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends AbstractControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Override
    protected Object setController() {
        return memberController;
    }

    @Test
    @DisplayName("회원 가입을 합니다. 성공할 경우 200 으로 응답합니다.")
    void 회원_가입_성공() throws Exception {
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("이름")
                .email("email@email.com")
                .passwd("password")
                .alias("별칭").build();

        MockHttpServletRequestBuilder builder = post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(memberJoinRequest));

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입을 하는 경우, 중복된 alias 인 경우 이미 존재하는 닉네임으로 인식하고 400 으로 응답합니다.")
    void 회원_가입_닉네임_중복() throws Exception{
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("이름")
                .email("email@email.com")
                .passwd("password")
                .alias("별칭").build();

        MockHttpServletRequestBuilder builder = post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(memberJoinRequest));

        when(memberService.join(any()))
                .thenThrow(new IllegalStateException("이미 존재하는 닉네임입니다."));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입을 하는 경우, 중복된 email 인 경우 이미 존재하는 회원으로 인식하고 400 으로 응답합니다.")
    void 회원_가입_이메일_중복() throws Exception{
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("이름")
                .email("email@email.com")
                .passwd("password")
                .alias("별칭").build();

        MockHttpServletRequestBuilder builder = post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(memberJoinRequest));

        when(memberService.join(any()))
                .thenThrow(new IllegalStateException("이미 존재하는 회원입니다."));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인을 성공한 경우 200 으로 응답합니다.")
    void 로그인_성공() throws Exception {
        LoginRequest loginRequest
                = new LoginRequest("email", "password");

        MockHttpServletRequestBuilder builder = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginRequest));

        when(memberService.login("email", "password"))
                .thenReturn(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 (가입한 이메일이 아닌 경우)")
    void 로그인_없는_이메일() throws Exception {
        LoginRequest loginRequest
                = new LoginRequest("email", "password");

        MockHttpServletRequestBuilder builder = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginRequest));

        when(memberService.login("email", "password"))
                .thenThrow(new NoSuchElementException("존재하지 않는 이메일 입니다."));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 실패 (비밀번호를 틀린 경우)")
    void 로그인_비밀번호_오류() throws Exception {
        LoginRequest loginRequest
                = new LoginRequest("email", "password");

        MockHttpServletRequestBuilder builder = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginRequest));

        when(memberService.login("email", "password"))
                .thenThrow(new IllegalStateException("비밀번호가 다릅니다."));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원의 id 값을 통해 회원의 정보를 조회합니다.")
    void 회원_조회_성공() throws Exception{
        MockHttpServletRequestBuilder builder = get("/member")
                .param("member_id", "1");

        when(memberService.getMemberById(1L))
                .thenReturn(Member.builder().build());

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 회원의 id 값을 통해 회원의 정보를 조회합니다. NoSuchElementException 이 발생하고, 400 을 응답합니다.")
    void 회원_조회_실패() throws Exception{
        MockHttpServletRequestBuilder builder = get("/member")
                .param("member_id", "1");

        when(memberService.getMemberById(1L))
                .thenThrow(new NoSuchElementException("존재하는 회원이 없습니다."));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경합니다.")
    void 비밀번호_변경() throws Exception{
        ChangePasswordRequest changePasswordRequest
                = new ChangePasswordRequest(1L, "newPasswd");

        MockHttpServletRequestBuilder builder = put("/member/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(changePasswordRequest));

        doNothing().when(memberService)
                .changePasswd(changePasswordRequest.getMember_id(), changePasswordRequest.getNewPasswd());

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원의 이미지를 추가하거나 변경합니다.")
    void 사진_추가_변경() throws Exception {
        MockMultipartFile file  // multipart file name 과 request param name 과 같아야 함
                = new MockMultipartFile("image", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        doNothing().when(memberService)
                .setImage(1L, file);

        MockHttpServletRequestBuilder builder = multipart("/member/image")
                .file(file)
                .param("member_id", "1");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원의 이미지를 삭제합니다. MultipartFile 이 없는 경우 사진이 삭제됩니다.")
    void 사진_삭제() throws Exception {
        MockHttpServletRequestBuilder builder = delete("/member/image")
                .param("member_id", "1");

        doNothing().when(memberService)
                .deleteImage(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원을 삭제합니다.")
    void 회원_삭제 () throws Exception {
        MockHttpServletRequestBuilder builder = delete("/member")
                .param("member_id", "1");

        doNothing().when(memberService)
                .deleteMember(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }
}