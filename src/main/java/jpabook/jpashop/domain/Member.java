package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")  // 한명이 여러개 주문 가능 (1대다), order 테이블의 member 필드에 매핑됨
    private List<Order> orders = new ArrayList<>();

}
