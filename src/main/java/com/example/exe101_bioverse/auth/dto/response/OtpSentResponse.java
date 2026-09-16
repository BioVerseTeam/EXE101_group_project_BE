package com.example.exe101_bioverse.auth.dto.response;

import com.example.exe101_bioverse.auth.enums.OtpPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpSentResponse {
    private String email;
    private OtpPurpose purpose;
    private long expiresInSeconds;
    private long resendAfterSeconds;
}
