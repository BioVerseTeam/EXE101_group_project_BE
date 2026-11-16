package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentResponse;
import com.example.exe101_bioverse.subscription.dto.response.PaymentSummaryResponse;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import com.example.exe101_bioverse.subscription.service.PaymentService;
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

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Admin Payments", description = "Quản lý và tra cứu các giao dịch thanh toán")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Tìm kiếm và phân trang danh sách các giao dịch thanh toán")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> searchPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<PaymentResponse> response = paymentService.searchPayments(
                status, keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/summary")
    @Operation(summary = "Lấy thống kê tổng quan doanh thu và các giao dịch thanh toán thành công")
    public ResponseEntity<ApiResponse<PaymentSummaryResponse>> getPaymentSummary() {
        PaymentSummaryResponse response = paymentService.getPaymentSummary();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/export")
    @Operation(summary = "Lấy danh sách các giao dịch thanh toán phục vụ xuất báo cáo Excel")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getExportPayments(
            @RequestParam(required = false) PaymentStatus status) {
        List<PaymentResponse> response = paymentService.getExportPayments(status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
