package jpabook.jpashop.api.repository;

import jpabook.jpashop.api.dto.OrderFlatDto;
import jpabook.jpashop.api.dto.OrderItemQueryDto;
import jpabook.jpashop.api.dto.OrderQueryDto;
import jpabook.jpashop.api.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderDtoRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
            "SELECT new jpabook.jpashop.api.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                "FROM Order o " +
                "JOIN o.member m " +
                "JOIN o.delivery d", OrderSimpleQueryDto.class)
            .getResultList();
    }


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return orders;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
            "SELECT new jpabook.jpashop.api.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                "FROM Order o " +
                "JOIN o.member m " +
                "JOIN o.delivery d", OrderQueryDto.class)
            .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
            "SELECT new jpabook.jpashop.api.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "FROM OrderItem oi " +
                "JOIN oi.item i " +
                "WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(orders));

        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return orders;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
            .map(OrderQueryDto::getOrderId)
            .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
            "SELECT new jpabook.jpashop.api.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "FROM OrderItem oi " +
                "JOIN oi.item i " +
                "WHERE oi.order.id IN :orderIds", OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

        return orderItems.stream()
            .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
            "SELECT new jpabook.jpashop.api.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count) " +
                "FROM Order o " +
                "JOIN o.member m " +
                "JOIN o.delivery d " +
                "JOIN o.orderItems oi " +
                "JOIN oi.item i", OrderFlatDto.class)
            .getResultList();
    }
}
