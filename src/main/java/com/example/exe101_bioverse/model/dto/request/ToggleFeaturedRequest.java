package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho Admin bật/tắt trạng thái phổ biến (featured) của model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToggleFeaturedRequest {

    @NotNull(message = "Trạng thái featured không được để trống")
    private Boolean isFeatured;

    /** Thứ tự hiển thị trên trang chủ (1, 2, 3, ...). */
    private Integer sortOrder;
}
