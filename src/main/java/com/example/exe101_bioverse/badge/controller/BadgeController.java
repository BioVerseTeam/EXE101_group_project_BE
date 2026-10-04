package com.example.exe101_bioverse.badge.controller;

import com.example.exe101_bioverse.badge.dto.response.BadgeResponse;
import com.example.exe101_bioverse.badge.service.StemBadgeService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@Tag(name = "STEM Badges", description = "API danh sách danh hiệu khoa học STEM công khai cho học sinh")
public class BadgeController {

    private final StemBadgeService badgeService;

    public BadgeController(StemBadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách các danh hiệu STEM đang mở công khai")
    public ResponseEntity<ApiResponse<List<BadgeResponse>>> listPublic() {
        return ResponseEntity.ok(ApiResponse.success(badgeService.listPublic()));
    }
}
