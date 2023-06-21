package com.mong.project.controller.member;

import com.mong.project.controller.member.dto.response.GetMemberResponse;
import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import com.mong.project.domain.member.annotation.Login;
import com.mong.project.controller.member.dto.request.ChangePasswordRequest;
import com.mong.project.controller.member.dto.request.LoginRequest;
import com.mong.project.controller.member.dto.request.MemberJoinRequest;
import com.mong.project.controller.board.dto.response.GetBoardResponse;
import com.mong.project.controller.member.dto.response.LoginResponse;
import com.mong.project.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.mong.project.config.interceptor.LoginConst.LOGIN_MEMBER;

@Slf4j
@RestController
@RequestMapping("/api/v2/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<GetMemberResponse> getMember(@Login final Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.ok().body(new GetMemberResponse(member));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login final Long memberId, final HttpSession session) {
        memberService.deleteMember(memberId);
        session.invalidate();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody MemberJoinRequest memberJoinRequest) {
        memberService.join(memberJoinRequest.toMember());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, final HttpSession session) {
        Long memberId = memberService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        session.setAttribute(LOGIN_MEMBER, memberId);
        LoginResponse loginResponse = new LoginResponse(memberId);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpSession session) {
        session.invalidate();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final Long memberId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changePasswd(memberId, changePasswordRequest.getNewPasswd());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/board")
    public ResponseEntity<List<GetBoardResponse>> getMemberBoards(@Login final Long memberId) {
        List<Board> boards = memberService.getMemberById(memberId).getBoards();
        List<GetBoardResponse> responses = boards.stream()
                .map(GetBoardResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> changeImage(@Login final Long memberId,  @RequestPart(name = "image") MultipartFile image) {
        memberService.setImage(memberId, image);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@Login final Long memberId) {
        memberService.deleteImage(memberId);

        return ResponseEntity.ok().build();
    }
}