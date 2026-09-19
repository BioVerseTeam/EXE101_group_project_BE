package com.example.exe101_bioverse.streak.controller;

import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.streak.dto.response.StreakResponse;
import com.example.exe101_bioverse.streak.service.StreakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/streak")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Streak", description = "Daily login streak. Miss a calendar day (Asia/Ho_Chi_Minh) and the current streak resets.")
public class StreakController {

    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }

    @GetMapping
    @Operation(summary = "Get the current user's streak without recording a visit")
    public ResponseEntity<ApiResponse<StreakResponse>> getStreak(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(streakService.getStreak(principal.getId())));
    }

    @PostMapping("/check-in")
    @Operation(summary = "Record today's visit and update the streak")
    public ResponseEntity<ApiResponse<StreakResponse>> checkIn(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(streakService.checkIn(principal.getId())));
    }
}
