package com.mong.MyProject.service.member;

import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.ImageType;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Slf4j
@Service
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
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void validateDuplicateAlias(Member member) {
        memberRepository.findByAlias(member.getAlias())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
    }

    /**
     * 로그인
     * */
    public Long login(String email, String passwd){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->{
                    throw new NoSuchElementException("존재하지 않는 이메일 입니다.");
                });

        if (!member.getPasswd().equals(passwd)) {
            throw new IllegalStateException("비밀번호가 다릅니다.");
        }

        return member.getId();
    }

    /**
     * id 로 회원 조회
     * */
    public Member getMemberById(Long member_id) {
        return memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException("존재하는 회원이 없습니다."));
    }

    /**
     * 회원 삭제
     * */
    public void deleteMember(Long member_id){
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException("존재하는 회원이 없습니다."));

        member.delete();
    }

    /**
     * 비밀번호 변경
     * */
    public void changePasswd(Long member_id, String passwd) {
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException("존재하는 회원이 없습니다."));

        member.setPasswd(passwd);
        memberRepository.save(member);
    }

    /**
     * member image 추가, 변경
     * */
    public void setImage(Long member_id, MultipartFile file) {
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()->new NoSuchElementException("존재하는 회원이 없습니다."));

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
                .orElseThrow(()->new NoSuchElementException("존재하는 회원이 없습니다."));

        member.setImage(null);
        memberRepository.save(member);
    }
}
