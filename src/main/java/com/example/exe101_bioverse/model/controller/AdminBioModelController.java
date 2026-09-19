package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.request.CreateModelRequest;
import com.example.exe101_bioverse.model.dto.request.ToggleFeaturedRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateModelRequest;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.service.AdminBioModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/models")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Bio Models", description = "API quản trị mô hình 3D - Chỉ dành cho Admin")
public class AdminBioModelController {

    private final AdminBioModelService adminBioModelService;

    public AdminBioModelController(AdminBioModelService adminBioModelService) {
        this.adminBioModelService = adminBioModelService;
    }

    @GetMapping
    @Operation(summary = "Danh sách model với bộ lọc (Admin)",
               description = "Bao gồm cả model ẩn/hiện. Lọc theo keyword, featured status, active status.")
    public ResponseEntity<ApiResponse<PageResponse<ModelDetailResponse>>> listModels(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                adminBioModelService.listModels(q, isFeatured, isActive, page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết model (Admin)")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> getModel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminBioModelService.getModel(id)));
    }

    @PostMapping
    @Operation(summary = "Tạo mô hình 3D mới")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> createModel(
            @Valid @RequestBody CreateModelRequest request) {
        ModelDetailResponse created = adminBioModelService.createModel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Tạo model thành công"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin mô hình 3D")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> updateModel(
            @PathVariable Long id,
            @Valid @RequestBody UpdateModelRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminBioModelService.updateModel(id, request), "Cập nhật model thành công"));
    }

    @PatchMapping("/{id}/featured")
    @Operation(summary = "Bật/tắt trạng thái phổ biến (featured)",
               description = "Cho phép Admin nhanh chóng bật/tắt cờ is_featured và gán thứ tự sort_order cho trang chủ.")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> toggleFeatured(
            @PathVariable Long id,
            @Valid @RequestBody ToggleFeaturedRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminBioModelService.toggleFeatured(id, request), "Cập nhật trạng thái phổ biến thành công"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mô hình 3D (soft delete)",
               description = "Chuyển is_active = false và tắt cờ featured thay vì xóa vĩnh viễn.")
    public ResponseEntity<ApiResponse<Void>> deleteModel(@PathVariable Long id) {
        adminBioModelService.deleteModel(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa model thành công"));
    }
}
