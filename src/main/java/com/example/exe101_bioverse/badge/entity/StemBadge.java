package com.example.exe101_bioverse.badge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stem_badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StemBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "filter_tag", nullable = false, length = 50)
    private String filterTag;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "criteria_type", nullable = false, length = 50)
    @Builder.Default
    private String criteriaType = "ALWAYS_UNLOCKED";

    @Column(name = "criteria_value", nullable = false)
    @Builder.Default
    private Integer criteriaValue = 0;

    @Column(name = "reward_xp", nullable = false)
    @Builder.Default
    private Integer rewardXp = 50;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "bg_unlocked", length = 50)
    @Builder.Default
    private String bgUnlocked = "bg-[#e8f5e9]";

    @Column(name = "border_unlocked", length = 50)
    @Builder.Default
    private String borderUnlocked = "border-[#2e7d32]";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (sortOrder == null) sortOrder = 0;
        if (isActive == null) isActive = true;
        if (criteriaValue == null) criteriaValue = 0;
        if (rewardXp == null) rewardXp = 50;
        if (bgUnlocked == null) bgUnlocked = "bg-[#e8f5e9]";
        if (borderUnlocked == null) borderUnlocked = "border-[#2e7d32]";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
