package com.example.exe101_bioverse.badge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBadgeRequest {

    @Size(max = 80, message = "Mã danh hiệu không vượt quá 80 ký tự")
    private String code;

    @NotBlank(message = "Tên danh hiệu không được để trống")
    @Size(max = 120, message = "Tên danh hiệu không vượt quá 120 ký tự")
    private String name;

    @NotBlank(message = "Biểu tượng/icon không được để trống")
    @Size(max = 50, message = "Icon không vượt quá 50 ký tự")
    private String icon;

    @NotBlank(message = "Tên nhóm không được để trống")
    @Size(max = 100, message = "Tên nhóm không vượt quá 100 ký tự")
    private String categoryName;

    @NotBlank(message = "Thẻ lọc không được để trống")
    @Size(max = 50, message = "Thẻ lọc không vượt quá 50 ký tự")
    private String filterTag;

    private String description;

    @Size(max = 50, message = "Loại điều kiện không vượt quá 50 ký tự")
    private String criteriaType;

    private Integer criteriaValue;

    private Integer rewardXp;

    private Integer sortOrder;

    private Boolean isActive;

    private String bgUnlocked;

    private String borderUnlocked;
}
