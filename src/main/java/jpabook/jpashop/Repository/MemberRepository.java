package jpabook.jpashop.Repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);  // PersistenceContext에 member 객체 저장 (트랜잭션 Commit 시점에 DB반영)
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // id : PK
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // JPQL : 대상이 테이블이 아닌 엔티티
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)  // 파라미터 바인딩
                .getResultList();
    }
}
