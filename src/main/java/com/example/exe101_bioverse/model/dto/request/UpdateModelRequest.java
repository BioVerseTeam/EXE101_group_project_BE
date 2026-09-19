package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho Admin cập nhật thông tin mô hình 3D.
 * Tất cả field đều optional - chỉ cập nhật field được gửi.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModelRequest {

    @Size(max = 255, message = "Tên model không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 255, message = "Tên tiếng Anh không được vượt quá 255 ký tự")
    private String nameEn;

    private String slug;

    @Size(max = 255, message = "Tên khoa học không được vượt quá 255 ký tự")
    private String scientificName;

    @Size(max = 100, message = "Thể loại không được vượt quá 100 ký tự")
    private String category;

    private String description;
    private String habitat;
    private String characteristics;
    private String classification;
    private String funFacts;

    private Integer grade;
    private String subject;

    @Size(max = 100)
    private String badgeText;

    @Size(max = 100)
    private String actionText;

    @Size(max = 50)
    private String actionIcon;

    @Size(max = 100)
    private String targetMode;

    @Size(max = 500)
    private String modelUrl;

    @Size(max = 500)
    private String thumbnailUrl;

    private String modelFormat;
    private Long modelSizeBytes;

    private BigDecimal defaultScale;
    private String defaultRotation;
    private String cameraPosition;
    private String annotations;

    private Boolean isFeatured;
    private Boolean isActive;
    private Integer sortOrder;
    private Long lessonId;
}
