package com.aipia.demo.order.dto;

import com.aipia.demo.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCreateRequestDto {

    private Long memberId;
}
