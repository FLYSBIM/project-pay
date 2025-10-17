package com.pomon.demo.payment.domain;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.member.domain.MemberRepository;
import com.pomon.demo.order.domain.Order;
import com.pomon.demo.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 컴포넌트만 로드하여 테스트 (인메모리 DB 사용)
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("결제 정보를 저장하고 ID로 조회")
    void saveAndFindById() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .password("123")
                .name("Joe")
                .balance(20000L)
                .build();
        Member savedMember = memberRepository.save(member);

        Order order = Order.builder()
                .member(savedMember)
                .price(10000L)
                .build();
        Order savedOrder = orderRepository.save(order);

        Payment payment = Payment.builder()
                .member(savedMember)
                .order(savedOrder)
                .amount(10000L)
                .build();

        // when
        Payment savedPayment = paymentRepository.save(payment);
        Payment foundPayment = paymentRepository.findById(savedPayment.getId()).get();

        // then
        assertThat(foundPayment.getId()).isEqualTo(savedPayment.getId());
        assertThat(foundPayment.getAmount()).isEqualTo(10000L);
        assertThat(foundPayment.getStatus()).isEqualTo(PaymentStatus.PENDING);

        assertThat(foundPayment.getMember().getId()).isEqualTo(savedMember.getId());
        assertThat(foundPayment.getOrder().getId()).isEqualTo(savedOrder.getId());
    }
}
