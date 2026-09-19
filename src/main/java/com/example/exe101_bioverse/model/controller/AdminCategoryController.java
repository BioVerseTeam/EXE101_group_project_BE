package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.model.dto.request.CreateCategoryRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateCategoryRequest;
import com.example.exe101_bioverse.model.dto.response.CategoryResponse;
import com.example.exe101_bioverse.model.service.BioModelCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/model-categories")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Model Categories", description = "Quản lý loại mẫu catalog")
public class AdminCategoryController {

    private final BioModelCategoryService categoryService;

    public AdminCategoryController(BioModelCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Danh sách loại mẫu (kể cả đang ẩn)")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.listAdmin()));
    }

    @PostMapping
    @Operation(summary = "Tạo loại mẫu")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryService.create(request), "Đã tạo loại mẫu"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Sửa loại mẫu")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.update(id, request), "Đã cập nhật loại mẫu"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ẩn loại mẫu")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã ẩn loại mẫu"));
    }
}
