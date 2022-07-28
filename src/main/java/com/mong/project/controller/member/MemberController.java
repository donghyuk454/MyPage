package com.mong.project.controller.member;

import com.mong.project.domain.member.Member;
import com.mong.project.dto.request.member.ChangePasswordRequest;
import com.mong.project.dto.request.member.LoginRequest;
import com.mong.project.dto.request.member.MemberJoinRequest;
import com.mong.project.dto.response.member.LoginResponse;
import com.mong.project.service.member.MemberService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/member")
    public ResponseEntity<Member> getMember(@RequestParam(name = "member_id") Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/member")
    public ResponseEntity<Void> join(@RequestBody MemberJoinRequest memberJoinRequest) {
        memberService.join(memberJoinRequest.toMember());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Long member_id = memberService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        LoginResponse loginResponse = new LoginResponse(member_id);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PutMapping("/member/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changePasswd(changePasswordRequest.getMemberId(), changePasswordRequest.getNewPasswd());

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/member/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> changeImage(@RequestParam(name = "member_id") Long id, @RequestParam(name = "image") MultipartFile image) {
        memberService.setImage(id, image);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member/image")
    public ResponseEntity<Void> deleteImage(@RequestParam(name = "member_id") Long memberId) {
        memberService.deleteImage(memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestParam Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().build();
    }
}