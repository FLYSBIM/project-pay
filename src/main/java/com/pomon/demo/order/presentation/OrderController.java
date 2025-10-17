package com.pomon.demo.order.presentation;

import com.pomon.demo.order.application.OrderService;
import com.pomon.demo.order.dto.OrderCreateRequestDto;
import com.pomon.demo.order.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(requestDto);
        URI location = URI.create("/api/orders/"+responseDto.getOrderId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        OrderResponseDto responseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(responseDto);
    }
}