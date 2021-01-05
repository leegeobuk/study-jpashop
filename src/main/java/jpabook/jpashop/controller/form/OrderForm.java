package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderForm {

    private Long memberId;
    private Long itemId;
    private int orderCount;
}
