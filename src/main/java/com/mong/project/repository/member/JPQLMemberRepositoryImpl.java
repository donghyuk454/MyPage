package com.mong.project.repository.member;

import com.mong.project.domain.member.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JPQLMemberRepositoryImpl implements JPQLMemberRepository {

    private final EntityManager em;

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByAlias(String alias) {
        List<Member> result = em.createQuery("select m from member m where m.alias=:alias", Member.class)
                .setParameter("alias", alias)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> result = em.createQuery("select m from member m where m.email=:email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmailAndAlias(String email, String alias) {
        List<Member> result = em.createQuery(
                "select m from member m where m.email=:email and m.alias=:alias", Member.class)
                .setParameter("email", email)
                .setParameter("alias", alias)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from member m", Member.class)
                .getResultList();
    }
}
