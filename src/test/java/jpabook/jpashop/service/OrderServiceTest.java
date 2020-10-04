package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("JPA 정복하기", 20000, 10); //이름, 가격, 재고
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문한 사품 종류 개수 확인", 1, order.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량", item.getPrice() * orderCount, order.getTotalPrice());
        assertEquals("주문 수량만큼 재고 줄어드는지 확인", 8, item.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("JPA 정복하기", 20000, 10); //이름, 가격, 재고

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, order.getStatus());
        assertEquals("주문 취소된 상품은 다시 재고가 증가해야 함", 10, item.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("JPA 정복하기", 20000, 10); //이름, 가격, 재고

        int orderCount = 20;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외(NotEnoughStockException)가 발생해야 함.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity) { Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

}