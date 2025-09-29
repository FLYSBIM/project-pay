package com.aipia.demo.order.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import com.aipia.demo.member.exception.MemberNotFoundException;
import com.aipia.demo.order.domain.Order;
import com.aipia.demo.order.domain.OrderRepository;
import com.aipia.demo.order.dto.OrderCreateRequestDto;
import com.aipia.demo.order.dto.OrderResponseDto;
import com.aipia.demo.order.exception.OrderNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder() {
        //given
        Long memberId = 1L;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.builder().memberId(memberId).build();

        Member mockMember = Member.builder().id(memberId).name("testUser").build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
        when(orderRepository.save(any(Order.class))).then(returnsFirstArg());

        //when
        OrderResponseDto responseDto = orderService.createOrder(requestDto);

        //then
        Assertions.assertThat(memberId).isEqualTo(responseDto.getMemberId());

        verify(orderRepository).save(any(Order.class));
    }

    // OrderServiceTest.java

    @Test
    @DisplayName("주문 조회 성공")
    void getOrder() {
        // given: 테스트 상황 설정
        Long orderId = 1L;
        Long memberId = 1L;
        Member mockMember = Member.builder().id(memberId).name("testUser").build();
        Order mockOrder = Order.builder().id(orderId).member(mockMember).price(10000).build();

        // orderRepository.findById(1L)이 호출되면, mockOrder를 담은 Optional을 반환하도록 설정
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));


        // when: 실제 테스트할 메소드 호출
        OrderResponseDto responseDto = orderService.getOrder(orderId);

        // then: 결과 검증
        Assertions.assertThat(responseDto.getOrderId()).isEqualTo(orderId);
        Assertions.assertThat(responseDto.getMemberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 주문 생성 시 예외 발생")
    void createOrder_fails_when_member_not_found() {
        // given
        Long nonExistentMemberId = 999L;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.builder().memberId(nonExistentMemberId).build();

        when(memberRepository.findById(nonExistentMemberId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            orderService.createOrder(requestDto);
        });
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID로 조회 시 예외 발생")
    void getOrder_fails_when_order_not_found() {
        // given
        Long nonExistentOrderId = 999L;

        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrder(nonExistentOrderId);
        });
    }
}
