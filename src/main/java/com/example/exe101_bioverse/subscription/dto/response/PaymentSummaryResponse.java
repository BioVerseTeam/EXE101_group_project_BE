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
public class PaymentSummaryResponse {

    private BigDecimal totalRevenue;
    private long paidCount;
    private BigDecimal todayRevenue;
    private BigDecimal monthRevenue;
    private long totalTransactions;
}
