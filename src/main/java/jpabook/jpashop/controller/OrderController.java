package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.OrderForm;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearchCriteria;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String showCreateOrderForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        model.addAttribute("orderForm", new OrderForm());
        return "orders/createOrderForm";
    }

    @PostMapping("/order")
    public String createOrder(OrderForm form) {
        orderService.order(form.getMemberId(), form.getItemId(), form.getOrderCount());
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String showOrderList(@ModelAttribute("orderSearch") OrderSearchCriteria searchCriteria,
                                Model model) {
        List<Order> orders = orderService.searchOrders(searchCriteria);
        model.addAttribute("orders", orders);
        return "orders/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
