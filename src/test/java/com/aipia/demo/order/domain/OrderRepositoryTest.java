package com.aipia.demo.order.domain;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveAndFindById() {
        // given
        Member member = Member.builder().email("test@test.com").password("123").name("Joe").build();
        memberRepository.save(member);

        Order order = Order.builder().member(member).price(10000L).build();

        // when
        Order savedOrder = orderRepository.save(order);
        Order foundOrder = orderRepository.findById(savedOrder.getId()).get();

        // then
        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.getMember().getEmail()).isEqualTo("test@test.com");
    }
}