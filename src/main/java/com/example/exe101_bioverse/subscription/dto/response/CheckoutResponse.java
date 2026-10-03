package com.example.exe101_bioverse.subscription.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private Long orderCode;
    private String checkoutUrl;
    private String qrCode;
    private BigDecimal amount;
    private String planName;
    private String description;
}
