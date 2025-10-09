package com.aipia.demo.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentRequestDto {
    private Long orderId;
    private Long memberId;
}
