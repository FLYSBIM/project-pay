package com.aipia.demo.payment.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import com.aipia.demo.member.exception.MemberNotFoundException;
import com.aipia.demo.order.domain.Order;
import com.aipia.demo.order.domain.OrderRepository;
import com.aipia.demo.order.exception.OrderNotFoundException;
import com.aipia.demo.payment.domain.PaymentRepository;
import com.aipia.demo.payment.dto.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentProcessor paymentProcessor;

    @Test
    @DisplayName("주문을 찾을 수 없을 때 OrderNotFoundException 발생")
    void createPayment_OrderNotFound() {
        // given
        long memberId = 1L;
        long nonExistentOrderId = 99L;
        PaymentRequestDto requestDto = PaymentRequestDto.builder().memberId(memberId).orderId(nonExistentOrderId).build();

        given(orderRepository.findById(nonExistentOrderId)).willReturn(Optional.empty());

        // when & then
        assertThrows(OrderNotFoundException.class, () -> {
            paymentService.createPayment(requestDto);
        });
    }

    @Test
    @DisplayName("회원을 찾을 수 없을 때 MemberNotFoundException 발생")
    void createPayment_MemberNotFound() {
        // given
        long nonExistentMemberId = 99L;
        long orderId = 1L;
        PaymentRequestDto requestDto = PaymentRequestDto.builder().orderId(orderId).memberId(nonExistentMemberId).build();
        Order order = Order.builder().id(orderId).price(5000L).build();
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            paymentService.createPayment(requestDto);
        });
    }

    @Test
    @DisplayName("잔액이 부족할 때 예외 발생 (RuntimeException 또는 커스텀 예외)")
    void createPayment_InsufficientBalance() {
        // given
        long memberId = 1L;
        long orderId = 1L;
        long initialBalance = 3000L; // 잔액 부족 상태
        long orderPrice = 5000L;

        PaymentRequestDto requestDto = PaymentRequestDto.builder().memberId(memberId).orderId(orderId).build();
        Member member = Member.builder().id(memberId).name("testUser").balance(initialBalance).build();
        Order order = Order.builder().id(orderId).member(member).price(orderPrice).build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(requestDto);
        });
    }
}