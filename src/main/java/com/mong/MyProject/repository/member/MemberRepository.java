package com.mong.MyProject.repository.member;

import com.mong.MyProject.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmailAndPasswd(String email, String passwd);
    Optional<Member> findByAlias(String alias);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
}
