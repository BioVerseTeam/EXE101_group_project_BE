package com.example.exe101_bioverse.subscription.dto.response;

import com.example.exe101_bioverse.subscription.entity.Payment;
import com.example.exe101_bioverse.subscription.enums.PaymentMethod;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userFullName;
    private Long subscriptionId;
    private String planName;
    private Long orderCode;
    private String payosPaymentLink;
    private String payosTransactionId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private PaymentMethod method;
    private String description;
    private LocalDateTime paidAt;
    private String failedReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentResponse from(Payment payment) {
        if (payment == null) return null;
        String planTitle = null;
        if (payment.getSubscription() != null && payment.getSubscription().getPlan() != null) {
            planTitle = payment.getSubscription().getPlan().getName();
        }

        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUser() != null ? payment.getUser().getId() : null)
                .userEmail(payment.getUser() != null ? payment.getUser().getEmail() : null)
                .userFullName(payment.getUser() != null ? payment.getUser().getFullName() : null)
                .subscriptionId(payment.getSubscription() != null ? payment.getSubscription().getId() : null)
                .planName(planTitle)
                .orderCode(payment.getOrderCode())
                .payosPaymentLink(payment.getPayosPaymentLink())
                .payosTransactionId(payment.getPayosTransactionId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .description(payment.getDescription())
                .paidAt(payment.getPaidAt())
                .failedReason(payment.getFailedReason())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
