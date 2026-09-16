package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.dto.request.ChangePasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateProfileRequest;
import com.example.exe101_bioverse.auth.dto.response.OtpSentResponse;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;

public interface ProfileService {

    UserResponse getMe(Long userId);

    UserResponse updateMe(Long userId, UpdateProfileRequest request);

    OtpSentResponse sendChangePasswordOtp(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);
}
