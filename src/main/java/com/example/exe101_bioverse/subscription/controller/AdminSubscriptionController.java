package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.dto.response.SubscriptionResponse;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import com.example.exe101_bioverse.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/subscriptions")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Admin Subscriptions", description = "Quản lý danh sách người dùng đăng ký gói Premium")
public class AdminSubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    @Operation(summary = "Tìm kiếm và phân trang danh sách các gói đăng ký của người dùng")
    public ResponseEntity<ApiResponse<PageResponse<SubscriptionResponse>>> searchSubscriptions(
            @RequestParam(required = false) SubscriptionStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<SubscriptionResponse> response = subscriptionService.searchSubscriptions(
                status, keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
