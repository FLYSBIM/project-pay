package com.pomon.demo.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCreateRequestDto {

    private Long memberId;
}
