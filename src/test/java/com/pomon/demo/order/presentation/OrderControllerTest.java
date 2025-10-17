package com.pomon.demo.order.presentation;

import com.pomon.demo.member.domain.Member;
import com.pomon.demo.order.application.OrderService;
import com.pomon.demo.order.domain.Order;
import com.pomon.demo.order.dto.OrderCreateRequestDto;
import com.pomon.demo.order.dto.OrderResponseDto;
import com.pomon.demo.order.exception.OrderNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OrderService memberService() {
            return Mockito.mock(OrderService.class);
        }
    }

    @Test
    @DisplayName("주문 생성 성공 시 201 Created 상태 코드를 응답한다")
    void createOrder_success() throws Exception {
        // given
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.builder().memberId(1L).build();
        OrderResponseDto responseDto = new OrderResponseDto(Order.builder().id(1L).price(10000L).member(Member.builder().id(1L).build()).build());

        when(orderService.createOrder(any(OrderCreateRequestDto.class))).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/1"))
                .andExpect(jsonPath("$.orderId").value(1L));
    }

    @Test
    @DisplayName("주문 조회 성공 시 200 OK 상태 코드를 응답")
    void getOrder_success() throws Exception {
        // given
        Long orderId = 1L;
        OrderResponseDto responseDto = new OrderResponseDto(Order.builder().id(orderId).price(10000L).member(Member.builder().id(1L).build()).build());

        when(orderService.getOrder(anyLong())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/orders/" + orderId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

    @Test
    @DisplayName("존재하지 않는 주문 조회 시 404 Not Found 상태 코드를 응답")
    void getOrder_notFound() throws Exception {
        // given
        Long nonExistentOrderId = 999L;

        when(orderService.getOrder(nonExistentOrderId))
                .thenThrow(new OrderNotFoundException("해당 ID의 주문을 찾을 수 없습니다."));

        // when
        ResultActions result = mockMvc.perform(get("/api/orders/" + nonExistentOrderId));

        // then
        result.andExpect(status().isNotFound());
    }
}