package com.mong.MyProject.repository.member;

import com.mong.MyProject.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private EntityManager em;
    private final EntityManagerFactory emf;

    @Autowired
    public MemberRepositoryImpl(final EntityManagerFactory emf) {
        this.emf = emf;
        this.em = emf.createEntityManager();
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

    @Override
    public Optional<Member> findByEmail(String email) {
        Member member = em.createQuery("select m from member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .get(0);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from member m", Member.class)
                .getResultList();
    }
}
