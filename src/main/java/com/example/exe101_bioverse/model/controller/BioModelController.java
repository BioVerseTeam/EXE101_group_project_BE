package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.dto.response.ModelSummaryResponse;
import com.example.exe101_bioverse.model.service.BioModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@Tag(name = "Bio Models", description = "API công khai cho mô hình 3D sinh học - Học sinh / Khách truy cập")
public class BioModelController {

    private final BioModelService bioModelService;

    public BioModelController(BioModelService bioModelService) {
        this.bioModelService = bioModelService;
    }

    @GetMapping("/featured")
    @Operation(summary = "Lấy danh sách mô hình phổ biến cho trang chủ",
               description = "Trả về các model 3D được Admin đánh dấu is_featured = true, sắp xếp theo sort_order ASC.")
    public ResponseEntity<ApiResponse<List<ModelSummaryResponse>>> getFeaturedModels() {
        return ResponseEntity.ok(ApiResponse.success(bioModelService.getFeaturedModels()));
    }

    @GetMapping("/catalog")
    @Operation(summary = "Danh mục mô hình 3D có phân trang và bộ lọc",
               description = "Hỗ trợ lọc theo khối lớp (grade), thể loại (category), môn học (subject) và tìm kiếm từ khóa (q).")
    public ResponseEntity<ApiResponse<PageResponse<ModelSummaryResponse>>> getCatalog(
            @Parameter(description = "Khối lớp: 6, 7, 8, 9") @RequestParam(required = false) Integer grade,
            @Parameter(description = "Thể loại: Hệ thần kinh, Hệ tiêu hóa,...") @RequestParam(required = false) String category,
            @Parameter(description = "Môn học: BIOLOGY, CHEMISTRY, PHYSICS") @RequestParam(required = false) String subject,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(bioModelService.getCatalog(grade, category, subject, q, page, size)));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Xem chi tiết mô hình 3D theo ID",
               description = "Tự động tăng lượt xem (views_count) mỗi lần gọi.")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> getModelById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bioModelService.getModelById(id)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Xem chi tiết mô hình 3D theo slug",
               description = "Dùng URL-friendly slug thay vì ID. Tự động tăng lượt xem.")
    public ResponseEntity<ApiResponse<ModelDetailResponse>> getModelBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(bioModelService.getModelBySlug(slug)));
    }

    @GetMapping("/categories")
    @Operation(summary = "Lấy danh sách thể loại mô hình",
               description = "Trả về các category riêng biệt để đổ vào thanh lọc trên UI. Có thể lọc theo môn học.")
    public ResponseEntity<ApiResponse<List<String>>> getCategories(
            @Parameter(description = "Môn học: BIOLOGY, CHEMISTRY, PHYSICS") @RequestParam(required = false) String subject
    ) {
        return ResponseEntity.ok(ApiResponse.success(bioModelService.getCategories(subject)));
    }
}
