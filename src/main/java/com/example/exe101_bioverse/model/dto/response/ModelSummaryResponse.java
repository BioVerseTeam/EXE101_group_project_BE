package com.example.exe101_bioverse.model.dto.response;

import com.example.exe101_bioverse.model.entity.BioModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO rút gọn cho các thẻ model trên trang chủ và danh mục.
 * Tối ưu payload - chỉ chứa thông tin cần thiết để render card UI.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelSummaryResponse {

    private Long id;
    private String name;
    private String nameEn;
    private String slug;
    private String category;
    private String description;
    private Integer grade;
    private String subject;
    private String thumbnailUrl;
    private String badgeText;
    private String actionText;
    private String actionIcon;
    private String targetMode;
    private Long viewsCount;

    public static ModelSummaryResponse from(BioModel model) {
        return ModelSummaryResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .nameEn(model.getNameEn())
                .slug(model.getSlug())
                .category(model.getCategory())
                .description(model.getDescription())
                .grade(model.getGrade())
                .subject(model.getSubject())
                .thumbnailUrl(model.getThumbnailUrl())
                .badgeText(model.getBadgeText())
                .actionText(model.getActionText())
                .actionIcon(model.getActionIcon())
                .targetMode(model.getTargetMode())
                .viewsCount(model.getViewsCount())
                .build();
    }
}
