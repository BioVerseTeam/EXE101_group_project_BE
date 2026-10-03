package com.example.exe101_bioverse.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @NotNull(message = "Mã gói cước không được để trống")
    private Long planId;

    /** Tùy chọn: URL chuyển hướng nếu muốn ghi đè cấu hình mặc định */
    private String returnUrl;
    private String cancelUrl;
}
