package com.example.exe101_bioverse.auth.controller;

import com.example.exe101_bioverse.auth.dto.request.ForgotPasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.LoginRequest;
import com.example.exe101_bioverse.auth.dto.request.LogoutRequest;
import com.example.exe101_bioverse.auth.dto.request.RefreshTokenRequest;
import com.example.exe101_bioverse.auth.dto.request.RegisterRequest;
import com.example.exe101_bioverse.auth.dto.request.ResendOtpRequest;
import com.example.exe101_bioverse.auth.dto.request.ResetPasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.VerifyOtpRequest;
import com.example.exe101_bioverse.auth.dto.response.AuthResponse;
import com.example.exe101_bioverse.auth.dto.response.OtpSentResponse;
import com.example.exe101_bioverse.auth.dto.response.ResetTokenResponse;
import com.example.exe101_bioverse.auth.service.AuthService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Register with OTP, login, forgot password, JWT refresh, and logout")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Start registration and send OTP to email")
    public ResponseEntity<ApiResponse<OtpSentResponse>> register(@Valid @RequestBody RegisterRequest request) {
        OtpSentResponse data = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Đã gửi mã OTP đến email"));
    }

    @PostMapping("/verify-register")
    @SecurityRequirements
    @Operation(summary = "Verify registration OTP and create the student account")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyRegister(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse data = authService.verifyRegister(request, httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data, "Đăng ký thành công"));
    }

    @PostMapping("/resend-otp")
    @SecurityRequirements
    @Operation(summary = "Resend OTP for registration or password reset")
    public ResponseEntity<ApiResponse<OtpSentResponse>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        OtpSentResponse data = authService.resendOtp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Đã gửi lại mã OTP"));
    }

    @PostMapping("/forgot-password")
    @SecurityRequirements
    @Operation(summary = "Send password-reset OTP to email")
    public ResponseEntity<ApiResponse<OtpSentResponse>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        OtpSentResponse data = authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Nếu email tồn tại, mã OTP đã được gửi"));
    }

    @PostMapping("/verify-reset-otp")
    @SecurityRequirements
    @Operation(summary = "Verify password-reset OTP and receive a reset token")
    public ResponseEntity<ApiResponse<ResetTokenResponse>> verifyResetOtp(@Valid @RequestBody VerifyOtpRequest request) {
        ResetTokenResponse data = authService.verifyResetOtp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Xác thực OTP thành công"));
    }

    @PostMapping("/reset-password")
    @SecurityRequirements
    @Operation(summary = "Set a new password using the reset token")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đặt lại mật khẩu thành công"));
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse data = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/refresh")
    @SecurityRequirements
    @Operation(summary = "Issue a new access token from a refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse data = authService.refresh(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout current session and blacklist access token in Redis")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request,
            HttpServletRequest httpRequest
    ) {
        authService.logout(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Logout all devices: revoke refresh sessions and invalidate JWTs in Redis")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Void>> logoutAll(HttpServletRequest httpRequest) {
        authService.logoutAll(httpRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
