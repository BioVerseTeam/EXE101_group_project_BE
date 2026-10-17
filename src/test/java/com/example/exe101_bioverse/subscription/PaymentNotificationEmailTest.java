package com.example.exe101_bioverse.subscription;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.service.MailService;
import com.example.exe101_bioverse.subscription.config.PayosConfig;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.entity.Payment;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.PaymentMethod;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import com.example.exe101_bioverse.subscription.repository.PaymentRepository;
import com.example.exe101_bioverse.subscription.service.SubscriptionService;
import com.example.exe101_bioverse.subscription.service.impl.PaymentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.payos.PayOS;
import vn.payos.model.webhooks.WebhookData;
import vn.payos.service.blocking.webhooks.WebhooksService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationEmailTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private PayosConfig payosConfig;

    @Mock
    private PayOS payOS;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User sampleUser;
    private Plan samplePlan;
    private Subscription sampleSubscription;
    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .email("student@bioverse.vn")
                .fullName("Nguyễn Văn Học")
                .build();

        samplePlan = Plan.builder()
                .id(10L)
                .name("Gói Học Sinh Kỳ I")
                .durationDays(90)
                .price(BigDecimal.valueOf(99000))
                .build();

        sampleSubscription = Subscription.builder()
                .id(100L)
                .user(sampleUser)
                .plan(samplePlan)
                .status(SubscriptionStatus.PENDING)
                .build();

        samplePayment = Payment.builder()
                .id(500L)
                .user(sampleUser)
                .subscription(sampleSubscription)
                .orderCode(123456L)
                .amount(BigDecimal.valueOf(99000))
                .currency("VND")
                .status(PaymentStatus.PENDING)
                .method(PaymentMethod.PAYOS)
                .build();
    }

    @Test
    @DisplayName("Webhook thành công -> Gửi email thanh toán thành công và đánh dấu emailNotifiedAt")
    void testProcessPayosWebhook_Success_SendsEmail() throws Exception {
        when(payosConfig.isConfigured()).thenReturn(true);
        WebhooksService webhookMock = mock(WebhooksService.class);
        when(payOS.webhooks()).thenReturn(webhookMock);

        WebhookData webhookData = new WebhookData();
        webhookData.setOrderCode(123456L);
        webhookData.setAmount(99000L);
        webhookData.setCode("00");
        webhookData.setReference("PAYOS_REF_001");

        when(webhookMock.verify(any())).thenReturn(webhookData);
        when(paymentRepository.findByOrderCode(123456L)).thenReturn(Optional.of(samplePayment));

        Subscription activatedSub = Subscription.builder()
                .id(100L)
                .user(sampleUser)
                .plan(samplePlan)
                .status(SubscriptionStatus.ACTIVE)
                .endDate(LocalDateTime.now().plusDays(90))
                .build();
        when(subscriptionService.activateSubscription(eq(1L), eq(samplePlan), eq(100L))).thenReturn(activatedSub);

        paymentService.processPayosWebhook(new Object());

        assertEquals(PaymentStatus.PAID, samplePayment.getStatus());
        assertNotNull(samplePayment.getEmailNotifiedAt());
        verify(mailService, times(1)).sendPaymentSuccess(
                eq("student@bioverse.vn"),
                eq("Nguyễn Văn Học"),
                eq(123456L),
                eq("Gói Học Sinh Kỳ I"),
                eq(BigDecimal.valueOf(99000)),
                any(),
                any(),
                eq("PAYOS_REF_001")
        );
    }

    @Test
    @DisplayName("Webhook không khớp số tiền -> Gửi email thanh toán thất bại")
    void testProcessPayosWebhook_AmountMismatch_SendsFailedEmail() throws Exception {
        when(payosConfig.isConfigured()).thenReturn(true);
        WebhooksService webhookMock = mock(WebhooksService.class);
        when(payOS.webhooks()).thenReturn(webhookMock);

        WebhookData webhookData = new WebhookData();
        webhookData.setOrderCode(123456L);
        webhookData.setAmount(50000L); // Expected 99000L
        webhookData.setCode("00");

        when(webhookMock.verify(any())).thenReturn(webhookData);
        when(paymentRepository.findByOrderCode(123456L)).thenReturn(Optional.of(samplePayment));

        paymentService.processPayosWebhook(new Object());

        assertEquals(PaymentStatus.FAILED, samplePayment.getStatus());
        assertNotNull(samplePayment.getEmailNotifiedAt());
        verify(mailService, times(1)).sendPaymentFailed(
                eq("student@bioverse.vn"),
                eq("Nguyễn Văn Học"),
                eq(123456L),
                eq("Gói Học Sinh Kỳ I"),
                eq(BigDecimal.valueOf(99000)),
                any(),
                contains("Số tiền thanh toán không khớp")
        );
        verify(mailService, never()).sendPaymentSuccess(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Người dùng hủy đơn qua cancelPayment -> Gửi email thông báo hủy/thất bại")
    void testCancelPayment_SendsFailedEmail() {
        when(paymentRepository.findByOrderCode(123456L)).thenReturn(Optional.of(samplePayment));
        when(payosConfig.isConfigured()).thenReturn(false);

        PaymentResponse response = paymentService.cancelPayment(1L, 123456L, false);

        assertEquals(PaymentStatus.CANCELLED, samplePayment.getStatus());
        assertNotNull(samplePayment.getEmailNotifiedAt());
        verify(mailService, times(1)).sendPaymentFailed(
                eq("student@bioverse.vn"),
                eq("Nguyễn Văn Học"),
                eq(123456L),
                eq("Gói Học Sinh Kỳ I"),
                eq(BigDecimal.valueOf(99000)),
                any(),
                eq("Người dùng hủy thanh toán")
        );
    }

    @Test
    @DisplayName("Idempotency: Không gửi email lần 2 nếu payment đã được đánh dấu PAID")
    void testEmailNotSentTwice_WhenAlreadyPaid() throws Exception {
        samplePayment.setStatus(PaymentStatus.PAID);
        samplePayment.setEmailNotifiedAt(LocalDateTime.now().minusMinutes(5));
        when(payosConfig.isConfigured()).thenReturn(true);
        WebhooksService webhookMock = mock(WebhooksService.class);
        when(payOS.webhooks()).thenReturn(webhookMock);

        WebhookData webhookData = new WebhookData();
        webhookData.setOrderCode(123456L);
        webhookData.setAmount(99000L);
        webhookData.setCode("00");

        when(webhookMock.verify(any())).thenReturn(webhookData);
        when(paymentRepository.findByOrderCode(123456L)).thenReturn(Optional.of(samplePayment));

        paymentService.processPayosWebhook(new Object());

        // verify mailService is never called
        verify(mailService, never()).sendPaymentSuccess(any(), any(), any(), any(), any(), any(), any(), any());
        verify(mailService, never()).sendPaymentFailed(any(), any(), any(), any(), any(), any(), any());
    }
}
