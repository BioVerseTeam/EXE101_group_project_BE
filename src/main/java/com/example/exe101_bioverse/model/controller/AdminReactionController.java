package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.model.dto.request.CreateReactionRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateReactionRequest;
import com.example.exe101_bioverse.model.dto.response.ReactionResponse;
import com.example.exe101_bioverse.model.service.ReactionEquationService;
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
@RequestMapping("/api/admin/reactions")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Reactions", description = "Cấu hình sẵn phương trình hoá học (.chemx) cho học sinh")
public class AdminReactionController {

    private final ReactionEquationService reactionService;

    public AdminReactionController(ReactionEquationService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping
    @Operation(summary = "Danh sách phương trình (kể cả đang ẩn)")
    public ResponseEntity<ApiResponse<List<ReactionResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(reactionService.listAdmin()));
    }

    @PostMapping
    @Operation(summary = "Thêm phương trình từ file .chemx")
    public ResponseEntity<ApiResponse<ReactionResponse>> create(@Valid @RequestBody CreateReactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reactionService.create(request), "Đã thêm phương trình"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Sửa phương trình")
    public ResponseEntity<ApiResponse<ReactionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReactionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.update(id, request), "Đã cập nhật phương trình"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ẩn phương trình (không xóa bài hệ thống)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reactionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã ẩn phương trình"));
    }
}
