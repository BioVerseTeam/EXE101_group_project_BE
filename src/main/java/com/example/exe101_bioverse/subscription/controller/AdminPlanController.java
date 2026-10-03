package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.subscription.dto.request.CreatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.request.UpdatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.response.PlanResponse;
import com.example.exe101_bioverse.subscription.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/plans")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Admin Subscription Plans", description = "Quản lý danh mục gói cước Premium cho Admin")
public class AdminPlanController {

    private final PlanService planService;

    @GetMapping
    @Operation(summary = "Lấy tất cả các gói cước (bao gồm cả gói ẩn/lưu trữ)")
    public ResponseEntity<ApiResponse<List<PlanResponse>>> listAllPlans() {
        return ResponseEntity.ok(ApiResponse.success(planService.listAdminPlans()));
    }

    @PostMapping
    @Operation(summary = "Tạo một gói cước mới")
    public ResponseEntity<ApiResponse<PlanResponse>> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(planService.createPlan(request), "Tạo gói cước thành công"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết một gói cước theo ID")
    public ResponseEntity<ApiResponse<PlanResponse>> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(planService.getPlanById(id)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @Operation(summary = "Cập nhật thông tin gói cước")
    public ResponseEntity<ApiResponse<PlanResponse>> updatePlan(
            @PathVariable Long id,
            @RequestBody UpdatePlanRequest request) {
        return ResponseEntity.ok(ApiResponse.success(planService.updatePlan(id, request), "Cập nhật gói cước thành công"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Lưu trữ gói cước (chuyển sang ARCHIVED, không xoá cứng)")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã chuyển gói cước sang trạng thái lưu trữ"));
    }
}
