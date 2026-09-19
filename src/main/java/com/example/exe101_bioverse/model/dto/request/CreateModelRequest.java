package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho Admin tạo mô hình 3D mới.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateModelRequest {

    @NotBlank(message = "Tên model không được để trống")
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

    @Size(max = 100, message = "Badge text không được vượt quá 100 ký tự")
    private String badgeText;

    @Size(max = 100, message = "Action text không được vượt quá 100 ký tự")
    private String actionText;

    @Size(max = 50, message = "Action icon không được vượt quá 50 ký tự")
    private String actionIcon;

    @Size(max = 100, message = "Target mode không được vượt quá 100 ký tự")
    private String targetMode;

    // 3D asset
    @Size(max = 500, message = "Model URL không được vượt quá 500 ký tự")
    private String modelUrl;

    @Size(max = 500, message = "Thumbnail URL không được vượt quá 500 ký tự")
    private String thumbnailUrl;

    private String modelFormat;
    private Long modelSizeBytes;

    // 3D display
    private BigDecimal defaultScale;
    private String defaultRotation;
    private String cameraPosition;
    private String annotations;

    // Flags
    private Boolean isFeatured;
    private Boolean isActive;
    private Integer sortOrder;
    private Long lessonId;
}
