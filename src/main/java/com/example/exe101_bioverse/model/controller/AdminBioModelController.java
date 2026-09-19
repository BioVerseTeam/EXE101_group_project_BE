package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.request.CreateModelRequest;
import com.example.exe101_bioverse.model.dto.request.ToggleFeaturedRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateModelRequest;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.service.AdminBioModelService;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/models")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Bio Models", description = "API quản trị mô hình 3D - Chỉ dành cho Admin")
public class AdminBioModelController {

    private static final Set<String> IMAGE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private final AdminBioModelService adminBioModelService;
    private final R2StorageService r2StorageService;

    public AdminBioModelController(AdminBioModelService adminBioModelService, R2StorageService r2StorageService) {
        this.adminBioModelService = adminBioModelService;
        this.r2StorageService = r2StorageService;
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

    @GetMapping("/assets")
    @Operation(summary = "Danh sách file 3D trên R2")
    public ResponseEntity<ApiResponse<List<ModelAssetResponse>>> listAssets() {
        return ResponseEntity.ok(ApiResponse.success(r2StorageService.listModels()));
    }

    @PostMapping(value = "/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload ảnh thumbnail đã crop lên R2")
    public ResponseEntity<ApiResponse<ModelAssetResponse>> uploadThumbnail(
            @RequestParam("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE, "Chưa chọn ảnh thumbnail");
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!IMAGE_TYPES.contains(contentType)) {
            throw new AppException(ErrorCode.INVALID_FILE, "Chỉ nhận JPG, PNG hoặc WebP");
        }
        if (file.getSize() > 8 * 1024 * 1024) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
        String ext = contentType.contains("png") ? "png" : contentType.contains("webp") ? "webp" : "jpg";
        String key = "thumbnails/" + UUID.randomUUID() + "." + ext;
        try {
            ModelAssetResponse uploaded = r2StorageService.uploadObject(key, file.getBytes(), contentType);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(uploaded, "Đã tải ảnh thumbnail"));
        } catch (IOException ex) {
            throw new AppException(ErrorCode.STORAGE_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mô hình 3D (soft delete)",
               description = "Chuyển is_active = false và tắt cờ featured thay vì xóa vĩnh viễn.")
    public ResponseEntity<ApiResponse<Void>> deleteModel(@PathVariable Long id) {
        adminBioModelService.deleteModel(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa model thành công"));
    }
}
