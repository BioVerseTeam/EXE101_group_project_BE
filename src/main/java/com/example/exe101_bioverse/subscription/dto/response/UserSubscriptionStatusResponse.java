package com.example.exe101_bioverse.subscription.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionStatusResponse {

    /**
     * True nếu hệ thống đang mở chế độ miễn phí toàn bộ để thu hút người dùng.
     */
    private Boolean isFreeAccessMode;

    /**
     * True nếu user đang có gói ACTIVE còn hạn hoặc là ADMIN.
     */
    private Boolean isPremium;

    private Long daysRemaining;

    private LocalDateTime expiresAt;

    private SubscriptionResponse activeSubscription;
}
