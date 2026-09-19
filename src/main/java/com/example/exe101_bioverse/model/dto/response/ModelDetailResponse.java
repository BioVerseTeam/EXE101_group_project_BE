package com.example.exe101_bioverse.model.dto.response;

import com.example.exe101_bioverse.model.entity.BioModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO chi tiết đầy đủ khi xem một model cụ thể.
 * Bao gồm thông tin giải phẫu, thiết lập camera 3D, annotations, và metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelDetailResponse {

    private Long id;
    private String name;
    private String nameEn;
    private String slug;
    private String scientificName;
    private String category;
    private String description;
    private String habitat;
    private String characteristics;
    private String classification;       // JSONB as String
    private String funFacts;             // JSONB as String
    private Integer grade;
    private String subject;
    private String badgeText;
    private String actionText;
    private String actionIcon;
    private String targetMode;

    // 3D asset
    private String modelUrl;
    private String thumbnailUrl;
    private String modelFormat;
    private Long modelSizeBytes;

    // 3D display settings
    private BigDecimal defaultScale;
    private String defaultRotation;      // JSONB as String
    private String cameraPosition;       // JSONB as String
    private String annotations;          // JSONB as String

    // Stats
    private Long viewsCount;
    private Boolean isFeatured;
    private Boolean isActive;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ModelDetailResponse from(BioModel model) {
        return ModelDetailResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .nameEn(model.getNameEn())
                .slug(model.getSlug())
                .scientificName(model.getScientificName())
                .category(model.getCategory())
                .description(model.getDescription())
                .habitat(model.getHabitat())
                .characteristics(model.getCharacteristics())
                .classification(model.getClassification())
                .funFacts(model.getFunFacts())
                .grade(model.getGrade())
                .subject(model.getSubject())
                .badgeText(model.getBadgeText())
                .actionText(model.getActionText())
                .actionIcon(model.getActionIcon())
                .targetMode(model.getTargetMode())
                .modelUrl(model.getModelUrl())
                .thumbnailUrl(model.getThumbnailUrl())
                .modelFormat(model.getModelFormat())
                .modelSizeBytes(model.getModelSizeBytes())
                .defaultScale(model.getDefaultScale())
                .defaultRotation(model.getDefaultRotation())
                .cameraPosition(model.getCameraPosition())
                .annotations(model.getAnnotations())
                .viewsCount(model.getViewsCount())
                .isFeatured(model.getIsFeatured())
                .isActive(model.getIsActive())
                .sortOrder(model.getSortOrder())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
