package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.dto.response.SubscriptionResponse;
import com.example.exe101_bioverse.subscription.dto.response.UserSubscriptionStatusResponse;
import com.example.exe101_bioverse.subscription.service.PaymentService;
import com.example.exe101_bioverse.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/subscription")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "User Subscription Info", description = "Thông tin gói cước và trạng thái Premium của người dùng hiện tại")
public class UserSubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Lấy trạng thái gói Premium hiện tại của người dùng")
    public ResponseEntity<ApiResponse<UserSubscriptionStatusResponse>> getMySubscriptionStatus(
            @AuthenticationPrincipal UserPrincipal principal) {
        UserSubscriptionStatusResponse response = subscriptionService.getUserStatus(
                principal != null ? principal.getId() : null,
                principal != null ? principal.getRole() : null);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/history")
    @Operation(summary = "Xem lịch sử các gói cước đã đăng ký của tôi")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getMySubscriptionHistory(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<SubscriptionResponse> response = subscriptionService.listUserSubscriptions(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/payments")
    @Operation(summary = "Xem lịch sử các giao dịch thanh toán của tôi")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPaymentHistory(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<PaymentResponse> response = paymentService.listUserPayments(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
