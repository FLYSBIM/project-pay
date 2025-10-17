package com.pomon.demo.order.domain;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.order.exception.InvalidOrderStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderDomainTest {

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("testUser")
                .password("abc")
                .email("abc@abc.com")
                .build();
    }

    @Test
    @DisplayName("빌더를 통해 Order 클래스 생성 성공")
    void createOrder() {
        //given
        Long price = 10000L;

        //when
        Order order = Order.builder()
                .member(testMember)
                .price(price)
                .build();

        //then
        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.getMember()).isEqualTo(testMember);
        Assertions.assertThat(order.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("Order 생성 시 OrderStatus는 ORDERED 상태여야 한다")
    void createOrder_DefaultStatus_isOrdered() {
        //given
        Long price = 10000L;

        //when
        Order order = Order.builder()
                .member(testMember)
                .price(price)
                .build();

        //then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    @DisplayName("주문을 취소하면 상태가 'CANCELED'로 변경된다")
    void cancelOrder_Changes_Status_To_CANCELED() {
        // given
        Order order = Order.builder()
                .member(testMember)
                .price(10000L)
                .build();

        // when
        order.cancel();

        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("주문을 완료하면 상태가 'COMPLETED'로 변경된다")
    void completeOrder_Changes_Status_To_COMPLETED() {
        // given
        Order order = Order.builder()
                .member(testMember)
                .price(10000L)
                .build();

        // when
        order.complete();

        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("이미 완료된 주문을 다시 완료하려 하면 예외 발생")
    void completeOrder_Already_Completed_Throws_Exception() {
        // given
        Order order = Order.builder()
                .member(testMember)
                .price(10000L)
                .build();
        order.complete();

        // when & then
        InvalidOrderStatusException exception = assertThrows(InvalidOrderStatusException.class, () -> {
            order.complete();
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("이미 완료된 주문입니다.");
    }

    @Test
    @DisplayName("이미 완료된 주문을 취소하려 하면 예외 발생")
    void cancelOrder_Already_Completed_Throws_Exception() {
        // given
        Order order = Order.builder()
                .member(testMember)
                .price(10000L)
                .build();
        order.complete();

        // when & then
        InvalidOrderStatusException exception = assertThrows(InvalidOrderStatusException.class, () -> {
            order.cancel();
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("이미 완료된 주문은 취소할 수 없습니다.");
    }
}