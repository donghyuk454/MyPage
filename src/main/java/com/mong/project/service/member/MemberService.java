package com.mong.project.service.member;

import com.mong.project.domain.image.Image;
import com.mong.project.domain.image.ImageType;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileService fileService;

    /**
     * 회원 가입
     * */
    public Long join(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);
        log.info("회원 가입 id = {}, name = {}, alias = {}, email = {}", member.getId(), member.getName(), member.getAlias(), member.getEmail());
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmailAndAlias(member.getEmail(), member.getAlias())
                .ifPresent(m->{throw new IllegalStateException(ErrorCode.ALREADY_EXIST_MEMBER);});
    }

    /**
     * 로그인
     * */
    public Long login(String email, String passwd){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException(ErrorCode.INVALID_EMAIL));

        if (isInvalidPassword(passwd, member)) {
            throw new IllegalStateException(ErrorCode.INVALID_PASSWORD);
        }

        return member.getId();
    }

    private static boolean isInvalidPassword(String passwd, Member member) {
        return !member.getPasswd().equals(passwd);
    }

    /**
     * id 로 회원 조회
     * */
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));
    }

    /**
     * 회원 삭제
     * */
    public void deleteMember(Long memberId){
        Member member = getMemberById(memberId);

        member.delete();
    }

    /**
     * 비밀번호 변경
     * */
    public void changePasswd(Long memberId, String passwd) {
        Member member = getMemberById(memberId);

        member.setPasswd(passwd);
        memberRepository.save(member);
    }

    /**
     * member image 추가, 변경
     * */
    public void setImage(Long memberId, MultipartFile file) {
        Member member = getMemberById(memberId);

        File imageFile = fileService.convertToFile(file);
        Image image = Image.builder()
                .url(imageFile.getAbsolutePath())
                .type(ImageType.MEMBER)
                .build();

        member.setImage(image);
        memberRepository.save(member);
    }

    /**
     * member image 삭제
     * */
    public void deleteImage(Long memberId){
        Member member = getMemberById(memberId);

        if (isSuccessToDeleteImage(member)) {
            member.setImage(null);
            memberRepository.save(member);
        }
    }

    private boolean isSuccessToDeleteImage(Member member) {
        return fileService.removeFileByPath(member.getImage().getUrl());
    }
}
