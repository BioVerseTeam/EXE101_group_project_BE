package com.example.exe101_bioverse.badge.controller;

import com.example.exe101_bioverse.badge.dto.request.CreateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.request.UpdateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.response.BadgeResponse;
import com.example.exe101_bioverse.badge.service.StemBadgeService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/badges")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Badges", description = "Quản lý danh hiệu khoa học STEM BioVerse")
public class AdminBadgeController {

    private final StemBadgeService badgeService;

    public AdminBadgeController(StemBadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả danh hiệu (kể cả đang ẩn)")
    public ResponseEntity<ApiResponse<List<BadgeResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(badgeService.listAdmin()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết danh hiệu theo ID")
    public ResponseEntity<ApiResponse<BadgeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(badgeService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Tạo danh hiệu mới")
    public ResponseEntity<ApiResponse<BadgeResponse>> create(@Valid @RequestBody CreateBadgeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(badgeService.create(request), "Đã tạo danh hiệu mới"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật danh hiệu")
    public ResponseEntity<ApiResponse<BadgeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBadgeRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(badgeService.update(id, request), "Đã cập nhật danh hiệu"));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Chuyển đổi trạng thái Ẩn/Hiện của danh hiệu")
    public ResponseEntity<ApiResponse<BadgeResponse>> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(badgeService.toggleStatus(id), "Đã chuyển đổi trạng thái danh hiệu"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ẩn danh hiệu khỏi hệ thống")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        badgeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã ẩn danh hiệu"));
    }
}
