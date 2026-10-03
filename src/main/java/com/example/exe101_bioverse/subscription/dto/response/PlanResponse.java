package com.example.exe101_bioverse.subscription.dto.response;

import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.enums.PlanDuration;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private PlanDuration duration;
    private Integer durationDays;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer discountPercentage;
    private List<String> features;
    private Integer maxDevices;
    private PlanStatus status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PlanResponse from(Plan plan) {
        if (plan == null) return null;

        Integer discount = null;
        if (plan.getOriginalPrice() != null && plan.getPrice() != null &&
                plan.getOriginalPrice().compareTo(BigDecimal.ZERO) > 0 &&
                plan.getOriginalPrice().compareTo(plan.getPrice()) > 0) {
            BigDecimal diff = plan.getOriginalPrice().subtract(plan.getPrice());
            discount = diff.multiply(BigDecimal.valueOf(100))
                    .divide(plan.getOriginalPrice(), 0, RoundingMode.HALF_UP)
                    .intValue();
        }

        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .slug(plan.getSlug())
                .description(plan.getDescription())
                .duration(plan.getDuration())
                .durationDays(plan.getDurationDays())
                .price(plan.getPrice())
                .originalPrice(plan.getOriginalPrice())
                .discountPercentage(discount)
                .features(plan.getFeatures())
                .maxDevices(plan.getMaxDevices())
                .status(plan.getStatus())
                .sortOrder(plan.getSortOrder())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
