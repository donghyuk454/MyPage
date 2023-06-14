package com.mong.project.repository.member;

import com.mong.project.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByAlias(String alias);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndAlias(String email, String alias);
    List<Member> findAll();
}
