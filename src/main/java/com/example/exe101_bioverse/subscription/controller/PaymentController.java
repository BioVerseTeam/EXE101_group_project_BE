package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.subscription.dto.request.CheckoutRequest;
import com.example.exe101_bioverse.subscription.dto.response.CheckoutResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments & Checkout", description = "Các API thanh toán qua cổng PayOS")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Tạo link thanh toán PayOS cho một gói cước")
    public ResponseEntity<ApiResponse<CheckoutResponse>> createCheckout(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = paymentService.createCheckout(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Khởi tạo đơn thanh toán PayOS thành công"));
    }

    @GetMapping("/{orderCode}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lấy trạng thái đơn thanh toán (hỗ trợ tự động đồng bộ từ PayOS)")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentStatus(
            @PathVariable Long orderCode,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(principal.getRole());
        PaymentResponse response = paymentService.getPaymentStatus(principal.getId(), orderCode, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{orderCode}/cancel")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Hủy đơn thanh toán đang chờ xử lý")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable Long orderCode,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(principal.getRole());
        PaymentResponse response = paymentService.cancelPayment(principal.getId(), orderCode, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Đã hủy đơn thanh toán"));
    }

    @PostMapping("/payos/webhook")
    @Operation(summary = "Webhook tiếp nhận kết quả thanh toán từ cổng PayOS")
    public ResponseEntity<Map<String, Object>> payosWebhook(@RequestBody Object webhookBody) {
        log.info("Received PayOS webhook request: {}", webhookBody);
        paymentService.processPayosWebhook(webhookBody);
        return ResponseEntity.ok(Map.of(
                "code", "00",
                "desc", "Success",
                "data", null
        ));
    }
}
