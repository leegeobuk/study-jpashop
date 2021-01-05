package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearchCriteria orderSearchCriteria) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;
        if (StringUtils.hasText(orderSearchCriteria.getMemberName())) {
            isFirstCondition = false;
            jpql += " where m.name like :name";
        }

        if (Objects.nonNull(orderSearchCriteria.getOrderStatus())) {
            jpql += isFirstCondition ? " where" : " and";
            jpql += " o.orderStatus = :status";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class);
        if (jpql.contains(":name")) {
            query.setParameter("name", orderSearchCriteria.getMemberName());
        }
        if (jpql.contains(":status")) {
            query.setParameter("status", orderSearchCriteria.getOrderStatus());
        }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
            "SELECT o FROM Order o " +
                "JOIN FETCH o.member m " +
                "JOIN FETCH o.delivery d", Order.class)
            .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
            "SELECT o FROM Order o " +
                "JOIN FETCH o.member m " +
                "JOIN FETCH o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
            "SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.member m " +
            "JOIN FETCH o.delivery d " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.item i", Order.class)
            .getResultList();
    }
}
