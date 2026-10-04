package com.example.exe101_bioverse.badge.dto.response;

import com.example.exe101_bioverse.badge.entity.StemBadge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeResponse {

    private Long id;
    private String code;
    private String name;
    private String icon;
    private String categoryName;
    private String filterTag;
    private String description;
    private String criteriaType;
    private Integer criteriaValue;
    private Integer rewardXp;
    private Integer sortOrder;
    private Boolean isActive;
    private String bgUnlocked;
    private String borderUnlocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BadgeResponse from(StemBadge badge) {
        if (badge == null) return null;
        return BadgeResponse.builder()
                .id(badge.getId())
                .code(badge.getCode())
                .name(badge.getName())
                .icon(badge.getIcon())
                .categoryName(badge.getCategoryName())
                .filterTag(badge.getFilterTag())
                .description(badge.getDescription())
                .criteriaType(badge.getCriteriaType())
                .criteriaValue(badge.getCriteriaValue())
                .rewardXp(badge.getRewardXp())
                .sortOrder(badge.getSortOrder())
                .isActive(badge.getIsActive())
                .bgUnlocked(badge.getBgUnlocked())
                .borderUnlocked(badge.getBorderUnlocked())
                .createdAt(badge.getCreatedAt())
                .updatedAt(badge.getUpdatedAt())
                .build();
    }
}
