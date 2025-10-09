package com.aipia.demo.payment.dto;

import com.aipia.demo.payment.domain.Payment;
import com.aipia.demo.payment.domain.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentResponseDto {
    private Long paymentId;
    private PaymentStatus paymentStatus;
    private Long orderId;
    private Long memberId;

    public PaymentResponseDto(Payment payment) {
        this.paymentId = payment.getId();
        this.paymentStatus = payment.getStatus();
        this.orderId = payment.getOrder().getId();
        this.memberId = payment.getMember().getId();
    }
}
