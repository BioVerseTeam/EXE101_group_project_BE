package com.example.exe101_bioverse.subscription.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.dto.request.CheckoutRequest;
import com.example.exe101_bioverse.subscription.dto.response.CheckoutResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    CheckoutResponse createCheckout(Long userId, CheckoutRequest request);

    void processPayosWebhook(Object webhookBody);

    PaymentResponse getPaymentStatus(Long userId, Long orderCode, boolean isAdmin);

    PaymentResponse cancelPayment(Long userId, Long orderCode, boolean isAdmin);

    List<PaymentResponse> listUserPayments(Long userId);

    PageResponse<PaymentResponse> searchPayments(PaymentStatus status, String keyword, Pageable pageable);
}
