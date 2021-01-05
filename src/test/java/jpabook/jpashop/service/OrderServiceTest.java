package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void orderTest() {
        //given
        Member member = createMember("member1", Address.of("Seoul", "Riverside", "123-123"));
        Item book = createBook("JPA, The ORM standard", 10000, 10);

        //when
        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order order = orderRepository.findById(orderId);

        //then
        assertEquals(OrderStatus.ORDER, order.getOrderStatus(), "When ordered, orderStatus is ORDER");
        assertEquals(1, order.getOrderItems().size(), "Number of types of orderItem should be exact");
        assertEquals(10000 * orderCount, order.getTotalPrice(), "Total price should be count * price");
        assertEquals(7, book.getStockQuantity(), "Stock of items should be updated");
    }

    @Test
    public void cancelOrderTest() {
        //given
        Member member = createMember("member1", Address.of("Seoul", "Riverside", "123-123"));
        Item book = createBook("JPA", 17000, 8);
        int orderCount = 11;

        //when


        //then
        assertThrows(NotEnoughStockException.class,
            () -> orderService.order(member.getId(), book.getId(), orderCount),
            "NotEnoughStockException should be thrown");
    }

    @Test
    public void outOfStockTest() {
        //given
        Member member = createMember("member3", Address.of("Seoul", "Road1", "123-123"));
        Item book = createBook("JPA3", 13000, 5);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findById(orderId);
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus(), "OrderStatus should be CANCEL");
        assertEquals(5, book.getStockQuantity(), "Stock of canceled item should be retrieved");
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}