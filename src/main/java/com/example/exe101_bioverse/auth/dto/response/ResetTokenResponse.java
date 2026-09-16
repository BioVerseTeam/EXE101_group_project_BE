package com.example.exe101_bioverse.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetTokenResponse {
    private String resetToken;
    private long expiresInSeconds;
}
