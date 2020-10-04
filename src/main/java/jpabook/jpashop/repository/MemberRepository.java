package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor  // final이 붙은 필드에 대해 빈 생성자 주입 (스프링부트에서 @PersistenceContext 대신 @Autowired도 허용하기 때문에 가능)
public class MemberRepository {

    //@PersistenceContext
    private final EntityManager em;

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
