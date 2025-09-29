package com.aipia.demo.order.domain;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.order.exception.InvalidOrderStatusException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "ORDER_PRICE")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    public void cancel() {
        if (this.orderStatus == OrderStatus.COMPLETED) {
            throw new InvalidOrderStatusException("이미 완료된 주문은 취소할 수 없습니다.");
        }

        this.orderStatus = OrderStatus.CANCELED;
    }

    public void complete() {
        if (this.orderStatus == OrderStatus.COMPLETED) {
            throw new InvalidOrderStatusException("이미 완료된 주문입니다.");
        }

        this.orderStatus = OrderStatus.COMPLETED;
    }
}