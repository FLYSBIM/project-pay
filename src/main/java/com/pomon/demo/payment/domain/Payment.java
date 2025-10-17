package com.pomon.demo.payment.domain;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.order.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENTS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "PAYMENT_AMOUNT",nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS",nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    public void complete() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("결제 대기 중인 상태에서만 완료 처리할 수 있습니다.");
        }
        this.status = PaymentStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("완료된 결제만 취소할 수 있습니다.");
        }
        this.status = PaymentStatus.CANCELLED;
    }
}