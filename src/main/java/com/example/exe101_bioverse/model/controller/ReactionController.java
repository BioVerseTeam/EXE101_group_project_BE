package com.example.exe101_bioverse.model.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.model.dto.response.ReactionResponse;
import com.example.exe101_bioverse.model.service.ReactionEquationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@Tag(name = "Reactions", description = "Phương trình hoá học đã cấu hình sẵn — học sinh / khách xem")
public class ReactionController {

    private final ReactionEquationService reactionService;

    public ReactionController(ReactionEquationService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping
    @Operation(summary = "Danh sách phương trình đang mở cho học sinh")
    public ResponseEntity<ApiResponse<List<ReactionResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(reactionService.listPublic()));
    }

    @GetMapping("/{code}")
    @Operation(summary = "Chi tiết một phương trình theo mã")
    public ResponseEntity<ApiResponse<ReactionResponse>> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.getPublicByCode(code)));
    }
}
