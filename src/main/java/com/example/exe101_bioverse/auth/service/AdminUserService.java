package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.dto.request.AdminUpdateUserRequest;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    PageResponse<UserResponse> listUsers(String q, UserStatus status, String role, Pageable pageable);

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, AdminUpdateUserRequest request, Long adminUserId);
}
