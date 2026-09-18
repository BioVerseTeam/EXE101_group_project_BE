package com.example.exe101_bioverse.auth.dto.response;

import com.example.exe101_bioverse.auth.enums.GenderType;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private Integer grade;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private GenderType gender;
    private String role;
    private UserStatus status;
    private Boolean emailVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
}
