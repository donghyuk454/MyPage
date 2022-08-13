package com.mong.project.controller.member;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import com.mong.project.dto.request.member.ChangePasswordRequest;
import com.mong.project.dto.request.member.LoginRequest;
import com.mong.project.dto.request.member.MemberJoinRequest;
import com.mong.project.dto.response.board.GetBoardResponse;
import com.mong.project.dto.response.member.LoginResponse;
import com.mong.project.service.member.MemberService;

import com.mong.project.util.transformer.MemberTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<Member> getMember(@RequestParam(name = "memberId") Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok().body(member);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@RequestParam(name = "memberId") Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody MemberJoinRequest memberJoinRequest) {
        memberService.join(MemberTransformer.joinRequestToMember(memberJoinRequest));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Long memberId = memberService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        LoginResponse loginResponse = new LoginResponse(memberId);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changePasswd(changePasswordRequest.getMemberId(), changePasswordRequest.getNewPasswd());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/board")
    public ResponseEntity<List<GetBoardResponse>> getMemberBoards(@RequestParam(name = "member_id") Long memberId) {
        List<Board> boards = memberService.getMemberById(memberId).getBoards();
        List<GetBoardResponse> responses = new ArrayList<>();
        boards.forEach(board -> {
            GetBoardResponse response = new GetBoardResponse(board);
            responses.add(response);
        });

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> changeImage(@RequestParam(name = "memberId") Long memberId, @RequestParam(name = "image") MultipartFile image) {
        memberService.setImage(memberId, image);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestParam(name = "memberId") Long memberId) {
        memberService.deleteImage(memberId);

        return ResponseEntity.ok().build();
    }
}