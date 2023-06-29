package com.mong.project.repository.member;

import com.mong.project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAlias(String alias);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndAlias(String email, String alias);
}
