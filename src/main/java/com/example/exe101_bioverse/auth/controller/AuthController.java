package com.example.exe101_bioverse.auth.controller;

import com.example.exe101_bioverse.auth.dto.request.LoginRequest;
import com.example.exe101_bioverse.auth.dto.request.LogoutRequest;
import com.example.exe101_bioverse.auth.dto.request.RefreshTokenRequest;
import com.example.exe101_bioverse.auth.dto.request.RegisterRequest;
import com.example.exe101_bioverse.auth.dto.response.AuthResponse;
import com.example.exe101_bioverse.auth.service.AuthService;
import com.example.exe101_bioverse.exam.dto.ApiResponse;
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
@Tag(name = "Auth", description = "Register, login, JWT refresh, and logout")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Register a new student account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse payload = authService.register(request, httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(success(payload));
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse payload = authService.login(request, httpRequest);
        return ResponseEntity.ok(success(payload));
    }

    @PostMapping("/refresh")
    @SecurityRequirements
    @Operation(summary = "Issue a new access token from a refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse payload = authService.refresh(request, httpRequest);
        return ResponseEntity.ok(success(payload));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout current session and blacklist access token in Redis")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request,
            HttpServletRequest httpRequest
    ) {
        authService.logout(request, httpRequest);
        return ResponseEntity.ok(success(null));
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Logout all devices: revoke refresh sessions and invalidate JWTs in Redis")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Void>> logoutAll(HttpServletRequest httpRequest) {
        authService.logoutAll(httpRequest);
        return ResponseEntity.ok(success(null));
    }

    private <T> ApiResponse<T> success(T payload) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("success");
        response.setPayload(payload);
        return response;
    }
}
