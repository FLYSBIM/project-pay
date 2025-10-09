package com.aipia.demo.payment.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import com.aipia.demo.member.exception.MemberNotFoundException;
import com.aipia.demo.order.domain.Order;
import com.aipia.demo.order.domain.OrderRepository;
import com.aipia.demo.order.domain.OrderStatus;
import com.aipia.demo.order.exception.OrderNotFoundException;
import com.aipia.demo.payment.domain.Payment;
import com.aipia.demo.payment.domain.PaymentRepository;
import com.aipia.demo.payment.dto.PaymentRequestDto;
import com.aipia.demo.payment.dto.PaymentResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentProcessor paymentProcessor;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {

        if(paymentRepository.existsById(requestDto.getOrderId())) {
            throw new IllegalStateException("이미 처리된 주문입니다.");
        }

        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("해당 ID의 주문을 찾지 못했습니다."));

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("해당 ID의 회원을 찾지 못했습니다."));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("주문자 정보와 결제자 정보가 일치하지 않습니다.");
        }

        if (order.getOrderStatus() != OrderStatus.ORDERED) {
            throw new IllegalStateException("결제 가능한 상태의 주문이 아닙니다.");
        }

        member.decreaseBalance(order.getPrice());
        order.complete();

        Payment payment = Payment.builder()
                .member(member)
                .order(order)
                .amount(order.getPrice())
                .build();
        paymentRepository.save(payment);

        paymentProcessor.completePaymentAfterDelay(payment.getId());

        return new PaymentResponseDto(payment);
    }
}