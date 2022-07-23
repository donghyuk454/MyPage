package com.mong.MyProject.service.member;

import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.ImageType;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.exception.ErrorCode;
import com.mong.MyProject.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     * */
    public Long join(Member member) {
        validateDuplicateMember(member);
        validateDuplicateAlias(member);

        memberRepository.save(member);
        log.info("회원 가입 id = {}, name = {}, alias = {}, email = {}", member.getId(), member.getName(), member.getAlias(), member.getEmail());
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException(ErrorCode.ALREADY_EXIST_MEMBER);
                });
    }

    private void validateDuplicateAlias(Member member) {
        memberRepository.findByAlias(member.getAlias())
                .ifPresent(m -> {
                    throw new IllegalStateException(ErrorCode.ALREADY_EXIST_ALIAS);
                });
    }

    /**
     * 로그인
     * */
    public Long login(String email, String passwd){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->{
                    throw new NoSuchElementException(ErrorCode.INVALID_EMAIL);
                });

        if (!member.getPasswd().equals(passwd)) {
            throw new IllegalStateException(ErrorCode.INVALID_PASSWORD);
        }

        return member.getId();
    }

    /**
     * id 로 회원 조회
     * */
    public Member getMemberById(Long member_id) {
        return memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));
    }

    /**
     * 회원 삭제
     * */
    public void deleteMember(Long member_id){
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));

        member.delete();
    }

    /**
     * 비밀번호 변경
     * */
    public void changePasswd(Long member_id, String passwd) {
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));

        member.setPasswd(passwd);
        memberRepository.save(member);
    }

    /**
     * member image 추가, 변경
     * */
    public void setImage(Long member_id, MultipartFile file) {
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));

        // TODO: image 생성
        String url = "";
        Image image = Image.builder()
                .url(url)
                .type(ImageType.MEMBER)
                .build();

        member.setImage(image);
        memberRepository.save(member);
    }

    /**
     * member image 삭제
     * */
    public void deleteImage(Long member_id){
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER));

        member.setImage(null);
        memberRepository.save(member);
    }
}
