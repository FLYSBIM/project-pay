package com.pomon.demo.payment.application;

import com.pomon.demo.payment.domain.Payment;
import com.pomon.demo.payment.domain.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;

    @Async
    @Transactional
    public void completePaymentAfterDelay(Long paymentId) {
        try {

            log.info("결제 완료 처리를 시작합니다. (7초 대기) Payment ID: {}", paymentId);
            Thread.sleep(7000);

            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다. ID: " + paymentId));

            payment.complete();
            log.info("결제 상태를 COMPLETED로 변경했습니다. Payment ID: {}", paymentId);

        } catch (InterruptedException e) {
            log.error("결제 처리 지연 중 스레드 오류 발생", e);
            Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
        } catch (EntityNotFoundException e) {
            log.error("비동기 결제 처리 중 엔티티를 찾지 못했습니다.", e);
        }
    }
}
