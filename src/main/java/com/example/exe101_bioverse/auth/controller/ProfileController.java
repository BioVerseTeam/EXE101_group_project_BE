package com.example.exe101_bioverse.auth.controller;

import com.example.exe101_bioverse.auth.dto.request.ChangePasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateProfileRequest;
import com.example.exe101_bioverse.auth.dto.response.OtpSentResponse;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.auth.service.ProfileService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profile", description = "Current user profile. Password change requires OTP.")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get the current user's profile and record today's streak check-in")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getMe(principal.getId())));
    }

    @PatchMapping
    @Operation(summary = "Update the current user's profile. Password and email cannot be changed here.")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(profileService.updateMe(principal.getId(), request)));
    }

    @PostMapping("/password/otp")
    @Operation(summary = "Send OTP to the current user's email before changing password")
    public ResponseEntity<ApiResponse<OtpSentResponse>> sendChangePasswordOtp(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        OtpSentResponse data = profileService.sendChangePasswordOtp(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(data, "Đã gửi mã OTP đến email"));
    }

    @PostMapping("/password")
    @Operation(summary = "Change password after verifying OTP")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        profileService.changePassword(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đổi mật khẩu thành công, vui lòng đăng nhập lại"));
    }
}
