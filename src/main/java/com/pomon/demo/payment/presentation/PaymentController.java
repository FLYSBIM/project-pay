package com.pomon.demo.payment.presentation;

import com.pomon.demo.payment.application.PaymentService;
import com.pomon.demo.payment.dto.PaymentRequestDto;
import com.pomon.demo.payment.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto responseDto = paymentService.createPayment(requestDto);
        URI location = URI.create("/api/payments/"+responseDto.getPaymentId());
        return ResponseEntity.created(location).body(responseDto);
    }
}