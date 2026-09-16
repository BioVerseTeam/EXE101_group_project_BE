package com.example.exe101_bioverse.auth.dto.request;

import com.example.exe101_bioverse.auth.enums.GenderType;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUpdateUserRequest {

    @Email(message = "Email is invalid")
    @Size(max = 255)
    private String email;

    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    private String avatarUrl;

    private LocalDate dateOfBirth;

    private GenderType gender;

    @Size(max = 50)
    private String role;

    private UserStatus status;

    private Boolean emailVerified;
}
