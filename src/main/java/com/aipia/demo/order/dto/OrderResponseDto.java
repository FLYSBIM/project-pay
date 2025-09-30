package com.aipia.demo.order.dto;

import com.aipia.demo.order.domain.Order;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private Long orderId;
    private Long memberId;
    private String memberName;
    private Long price;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.memberId = order.getMember().getId();
        this.memberName = order.getMember().getName();
        this.price = order.getPrice();
    }
}