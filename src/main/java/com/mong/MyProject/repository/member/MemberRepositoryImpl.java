package com.mong.MyProject.repository.member;

import com.mong.MyProject.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private EntityManager em;

    @Autowired
    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

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
    public Optional<Member> findByEmailAndPasswd(String email, String passwd) {
        Member member = em.createQuery("select m from member m where m.email = :email and m.passwd = :passwd", Member.class)
                .setParameter("email", email)
                .setParameter("passwd", passwd)
                .getResultList()
                .get(0);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByAlias(String alias) {
        Member member = em.createQuery("select m from member m where m.alias = :alias", Member.class)
                .setParameter("alias", alias)
                .getResultList()
                .get(0);
        return Optional.ofNullable(member);
    }
}