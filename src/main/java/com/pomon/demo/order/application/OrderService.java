package com.pomon.demo.order.application;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.member.domain.MemberRepository;
import com.pomon.demo.member.exception.MemberNotFoundException;
import com.pomon.demo.order.domain.Order;
import com.pomon.demo.order.domain.OrderRepository;
import com.pomon.demo.order.dto.OrderCreateRequestDto;
import com.pomon.demo.order.dto.OrderResponseDto;
import com.pomon.demo.order.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository) {

        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
    }

    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto) {

        Long memberId = requestDto.getMemberId();
        Long price = tmpRandomPrice(); //임시 사용 가격생성 메서드

        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalMember.isEmpty()){
            throw new MemberNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        }

        Order order = Order.builder().member(optionalMember.get()).price(price).build();
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    public OrderResponseDto getOrder(Long orderId){

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if(optionalOrder.isEmpty()){
            throw new OrderNotFoundException("해당 id의 주문을 찾을 수 없습니다.");
        }

        return new OrderResponseDto(optionalOrder.get());
    }

    public Long tmpRandomPrice(){
        return 5000L;
    }
}