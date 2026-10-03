package com.example.exe101_bioverse.subscription.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.subscription.dto.response.PlanResponse;
import com.example.exe101_bioverse.subscription.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@Tag(name = "Subscription Plans (Public)", description = "Bảng giá và các gói cước đang mở bán cho học sinh")
public class PlanController {

    private final PlanService planService;

    @GetMapping
    @Operation(summary = "Lấy danh sách các gói cước đang mở bán")
    public ResponseEntity<ApiResponse<List<PlanResponse>>> listPublicPlans() {
        return ResponseEntity.ok(ApiResponse.success(planService.listPublicPlans()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết một gói cước theo ID")
    public ResponseEntity<ApiResponse<PlanResponse>> getPlan(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(planService.getPlanById(id)));
    }
}
