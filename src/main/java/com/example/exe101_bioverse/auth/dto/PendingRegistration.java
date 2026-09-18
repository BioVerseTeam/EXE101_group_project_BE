package com.example.exe101_bioverse.auth.dto;

import com.example.exe101_bioverse.auth.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingRegistration {
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private Integer grade;
    private LocalDate dateOfBirth;
    private GenderType gender;
}
