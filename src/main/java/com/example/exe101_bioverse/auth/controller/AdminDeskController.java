package com.example.exe101_bioverse.auth.controller;

import com.example.exe101_bioverse.auth.dto.response.AdminDeskResponse;
import com.example.exe101_bioverse.auth.service.AdminDeskService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/desk")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Desk", description = "Intake snapshot for the admin dashboard")
public class AdminDeskController {

    private final AdminDeskService adminDeskService;

    public AdminDeskController(AdminDeskService adminDeskService) {
        this.adminDeskService = adminDeskService;
    }

    @GetMapping
    @Operation(summary = "Load models, users, and R2 counts in one response")
    public ResponseEntity<ApiResponse<AdminDeskResponse>> loadDesk() {
        return ResponseEntity.ok(ApiResponse.success(adminDeskService.load()));
    }
}
