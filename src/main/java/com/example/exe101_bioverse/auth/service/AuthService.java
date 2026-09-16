package com.example.exe101_bioverse.auth.service;

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
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    OtpSentResponse register(RegisterRequest request);

    AuthResponse verifyRegister(VerifyOtpRequest request, HttpServletRequest httpRequest);

    OtpSentResponse resendOtp(ResendOtpRequest request);

    OtpSentResponse forgotPassword(ForgotPasswordRequest request);

    ResetTokenResponse verifyResetOtp(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);

    AuthResponse refresh(RefreshTokenRequest request, HttpServletRequest httpRequest);

    void logout(LogoutRequest request, HttpServletRequest httpRequest);

    void logoutAll(HttpServletRequest httpRequest);
}
