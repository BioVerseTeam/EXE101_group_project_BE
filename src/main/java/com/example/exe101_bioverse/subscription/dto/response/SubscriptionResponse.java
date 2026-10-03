package com.example.exe101_bioverse.subscription.dto.response;

import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userFullName;
    private PlanResponse plan;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean autoRenew;
    private String cancelReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SubscriptionResponse from(Subscription sub) {
        if (sub == null) return null;
        return SubscriptionResponse.builder()
                .id(sub.getId())
                .userId(sub.getUser() != null ? sub.getUser().getId() : null)
                .userEmail(sub.getUser() != null ? sub.getUser().getEmail() : null)
                .userFullName(sub.getUser() != null ? sub.getUser().getFullName() : null)
                .plan(PlanResponse.from(sub.getPlan()))
                .status(sub.getStatus())
                .startDate(sub.getStartDate())
                .endDate(sub.getEndDate())
                .autoRenew(sub.getAutoRenew())
                .cancelReason(sub.getCancelReason())
                .createdAt(sub.getCreatedAt())
                .updatedAt(sub.getUpdatedAt())
                .build();
    }
}
