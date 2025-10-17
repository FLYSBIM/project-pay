package com.pomon.demo.payment.domain;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentDomainTest {

    @Test
    @DisplayName("Payment 생성 시 기본 상태는 PENDING 이다")
    void createPayment_defaultStatus_isPending() {
        // given
        Member member = Member.builder().build();
        Order order = Order.builder().build();

        // when
        Payment payment = Payment.builder()
                .member(member)
                .order(order)
                .amount(10000L)
                .build();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payment.getAmount()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("PENDING 상태의 결제를 성공 처리하면 COMPLETED 상태가 된다")
    void completePayment_success() {
        // given
        Payment payment = Payment.builder().status(PaymentStatus.PENDING).build();

        // when
        payment.complete();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    @DisplayName("이미 완료된 결제를 다시 성공 처리하려 하면 예외가 발생한다")
    void completePayment_fail_if_already_completed() {
        // given
        Payment payment = Payment.builder().status(PaymentStatus.COMPLETED).build();

        // when, then
        assertThrows(IllegalStateException.class, payment::complete);
    }

    @Test
    @DisplayName("COMPLETED 상태의 결제를 취소 처리하면 CANCELLED 상태가 된다")
    void cancelPayment_success() {
        // given
        Payment payment = Payment.builder().status(PaymentStatus.COMPLETED).build();

        // when
        payment.cancel();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    @DisplayName("PENDING 상태의 결제를 취소하려 하면 예외가 발생한다")
    void cancelPayment_fail_if_pending() {
        // given
        Payment payment = Payment.builder().status(PaymentStatus.PENDING).build();

        // when, then
        assertThrows(IllegalStateException.class, payment::cancel);
    }
}
