package com.example.exe101_bioverse.subscription.service.impl;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.config.PayosConfig;
import com.example.exe101_bioverse.subscription.dto.request.CheckoutRequest;
import com.example.exe101_bioverse.subscription.dto.response.CheckoutResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.entity.Payment;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.PaymentMethod;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import com.example.exe101_bioverse.subscription.repository.PaymentRepository;
import com.example.exe101_bioverse.subscription.service.PaymentService;
import com.example.exe101_bioverse.subscription.service.PlanService;
import com.example.exe101_bioverse.subscription.service.SubscriptionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.v2.paymentRequests.PaymentLinkStatus;
import vn.payos.model.webhooks.WebhookData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final PayosConfig payosConfig;
    private final PayOS payOS;
    private final ObjectMapper objectMapper;

    private PayOS getActivePayOS() {
        if (payosConfig.isConfigured()) {
            return new PayOS(
                    payosConfig.getClientId().trim(),
                    payosConfig.getApiKey().trim(),
                    payosConfig.getChecksumKey().trim()
            );
        }
        return payOS;
    }

    @Override
    public CheckoutResponse createCheckout(Long userId, CheckoutRequest request) {
        if (!payosConfig.isConfigured()) {
            throw new AppException(ErrorCode.PAYOS_CONFIG_MISSING);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Plan plan = planService.getEntityById(request.getPlanId());
        if (plan.getStatus() != PlanStatus.ACTIVE) {
            throw new AppException(ErrorCode.PLAN_NOT_ACTIVE);
        }

        // Tạo orderCode duy nhất: timestamp giây + 3 số ngẫu nhiên
        long baseTime = System.currentTimeMillis() / 1000;
        int randomSuffix = ThreadLocalRandom.current().nextInt(100, 999);
        Long orderCode = (baseTime % 10000000) * 1000 + randomSuffix;

        long amount = plan.getPrice().longValue();
        String description = "BioVerse " + (orderCode % 100000);
        if (description.length() > 25) {
            description = description.substring(0, 25);
        }

        String returnUrl = (request.getReturnUrl() != null && !request.getReturnUrl().isBlank())
                ? request.getReturnUrl()
                : payosConfig.getReturnUrl() + "?orderCode=" + orderCode;

        String cancelUrl = (request.getCancelUrl() != null && !request.getCancelUrl().isBlank())
                ? request.getCancelUrl()
                : payosConfig.getCancelUrl() + "?orderCode=" + orderCode + "&status=CANCELLED";

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name(plan.getName())
                .quantity(1)
                .price(amount)
                .build();

        CreatePaymentLinkRequest payosRequest = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .items(Collections.singletonList(item))
                .buyerName(user.getFullName())
                .buyerEmail(user.getEmail())
                .buyerPhone(user.getPhone() != null ? user.getPhone() : "")
                .build();

        CreatePaymentLinkResponse payosResponse;
        try {
            payosResponse = getActivePayOS().paymentRequests().create(payosRequest);
        } catch (PayOSException e) {
            log.error("Error creating payment link via PayOS: ", e);
            throw new AppException(ErrorCode.PAYOS_SERVICE_ERROR, "Không thể khởi tạo link PayOS: " + e.getMessage());
        }

        // Tạo subscription PENDING
        Subscription sub = subscriptionService.createPendingSubscription(userId, plan);

        // Tạo payment PENDING
        Payment payment = Payment.builder()
                .user(user)
                .subscription(sub)
                .orderCode(orderCode)
                .payosPaymentLink(payosResponse.getCheckoutUrl())
                .payosTransactionId(payosResponse.getPaymentLinkId())
                .amount(plan.getPrice())
                .currency("VND")
                .status(PaymentStatus.PENDING)
                .method(PaymentMethod.PAYOS)
                .description(description)
                .build();

        paymentRepository.save(payment);

        return CheckoutResponse.builder()
                .orderCode(orderCode)
                .checkoutUrl(payosResponse.getCheckoutUrl())
                .qrCode(payosResponse.getQrCode())
                .amount(plan.getPrice())
                .planName(plan.getName())
                .description(description)
                .build();
    }

    @Override
    public void processPayosWebhook(Object webhookBody) {
        if (!payosConfig.isConfigured()) {
            throw new AppException(ErrorCode.PAYOS_CONFIG_MISSING);
        }

        WebhookData webhookData;
        try {
            webhookData = getActivePayOS().webhooks().verify(webhookBody);
        } catch (Exception e) {
            log.warn("Failed to verify PayOS webhook signature: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_WEBHOOK_SIGNATURE);
        }

        if (webhookData == null) {
            log.warn("Verified webhook data is null");
            return;
        }

        Long orderCode = webhookData.getOrderCode();
        Payment payment = paymentRepository.findByOrderCode(orderCode)
                .orElse(null);

        if (payment == null) {
            log.warn("Payment with orderCode {} not found in database for webhook", orderCode);
            return;
        }

        // Idempotent: nếu đã PAID thì không xử lý lại
        if (payment.getStatus() == PaymentStatus.PAID) {
            log.info("Payment orderCode {} has already been marked as PAID", orderCode);
            return;
        }

        // Lưu thông tin audit webhook
        try {
            Map<String, Object> dataMap = objectMapper.convertValue(webhookData, new TypeReference<Map<String, Object>>() {});
            payment.setWebhookData(dataMap);
        } catch (Exception e) {
            log.warn("Could not serialize webhookData: {}", e.getMessage());
        }

        // Kiểm tra số tiền
        BigDecimal expectedAmount = payment.getAmount();
        BigDecimal receivedAmount = BigDecimal.valueOf(webhookData.getAmount());
        if (expectedAmount.compareTo(receivedAmount) != 0) {
            log.error("Amount mismatch for orderCode {}: expected {}, received {}", orderCode, expectedAmount, receivedAmount);
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailedReason("Số tiền thanh toán không khớp: dự kiến " + expectedAmount + ", thực nhận " + receivedAmount);
            paymentRepository.save(payment);
            return;
        }

        // Đánh dấu PAID và kích hoạt subscription
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        if (webhookData.getReference() != null) {
            payment.setPayosTransactionId(webhookData.getReference());
        }
        paymentRepository.save(payment);

        if (payment.getSubscription() != null && payment.getSubscription().getPlan() != null) {
            subscriptionService.activateSubscription(
                    payment.getUser().getId(),
                    payment.getSubscription().getPlan(),
                    payment.getSubscription().getId()
            );
        }

        log.info("Payment orderCode {} processed successfully via webhook!", orderCode);
    }

    @Override
    public PaymentResponse getPaymentStatus(Long userId, Long orderCode, boolean isAdmin) {
        Payment payment = paymentRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!isAdmin && (userId == null || !payment.getUser().getId().equals(userId))) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        // Nếu đơn đang PENDING, chủ động sync từ PayOS (đặc biệt hữu ích khi dev localhost không có public webhook)
        if (payment.getStatus() == PaymentStatus.PENDING && payosConfig.isConfigured()) {
            try {
                PaymentLink linkInfo = getActivePayOS().paymentRequests().get(orderCode);
                if (linkInfo != null && linkInfo.getStatus() == PaymentLinkStatus.PAID) {
                    log.info("Actively synced payment status PAID from PayOS for orderCode {}", orderCode);
                    payment.setStatus(PaymentStatus.PAID);
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);

                    if (payment.getSubscription() != null && payment.getSubscription().getPlan() != null) {
                        subscriptionService.activateSubscription(
                                payment.getUser().getId(),
                                payment.getSubscription().getPlan(),
                                payment.getSubscription().getId()
                        );
                    }
                } else if (linkInfo != null && linkInfo.getStatus() == PaymentLinkStatus.CANCELLED) {
                    payment.setStatus(PaymentStatus.CANCELLED);
                    paymentRepository.save(payment);
                }
            } catch (Exception e) {
                log.warn("Could not actively sync payment status from PayOS: {}", e.getMessage());
            }
        }

        return PaymentResponse.from(payment);
    }

    @Override
    public PaymentResponse cancelPayment(Long userId, Long orderCode, boolean isAdmin) {
        Payment payment = paymentRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!isAdmin && (userId == null || !payment.getUser().getId().equals(userId))) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new AppException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setFailedReason("Người dùng hủy thanh toán");
        paymentRepository.save(payment);

        if (payosConfig.isConfigured()) {
            try {
                getActivePayOS().paymentRequests().cancel(orderCode, "User cancelled");
            } catch (Exception e) {
                log.warn("Failed to cancel payment on PayOS: {}", e.getMessage());
            }
        }

        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> listUserPayments(Long userId) {
        return paymentRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> searchPayments(PaymentStatus status, String keyword, Pageable pageable) {
        Page<Payment> page;
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        if (kw != null) {
            String pattern = "%" + kw.toLowerCase() + "%";
            page = paymentRepository.searchWithKeyword(status, pattern, pageable);
        } else if (status != null) {
            page = paymentRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
        } else {
            page = paymentRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return PageResponse.<PaymentResponse>builder()
                .items(page.getContent().stream().map(PaymentResponse::from).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .build();
    }
}
