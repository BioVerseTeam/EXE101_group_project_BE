package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.model.dto.request.CreateLabRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateLabRequest;
import com.example.exe101_bioverse.model.dto.response.LabResponse;
import com.example.exe101_bioverse.model.service.BioLabService;
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
@RequestMapping("/api/admin/labs")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Labs", description = "Quản lý lab 3D gắn với nhân mẫu")
public class AdminLabController {

    private final BioLabService labService;

    public AdminLabController(BioLabService labService) {
        this.labService = labService;
    }

    @GetMapping
    @Operation(summary = "Danh sách lab (kể cả đang ẩn)")
    public ResponseEntity<ApiResponse<List<LabResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(labService.listAdmin()));
    }

    @PostMapping
    @Operation(summary = "Tạo lab")
    public ResponseEntity<ApiResponse<LabResponse>> create(@Valid @RequestBody CreateLabRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(labService.create(request), "Đã tạo lab"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Sửa lab")
    public ResponseEntity<ApiResponse<LabResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLabRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(labService.update(id, request), "Đã cập nhật lab"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ẩn lab (không xóa lab hệ thống)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        labService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã ẩn lab"));
    }
}
