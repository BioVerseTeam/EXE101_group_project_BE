package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReactionRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String subtitle;

    @Size(max = 50)
    private String gradeLabel;

    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Size(max = 100)
    private String code;

    /** Nội dung .chemx (JSON string hoặc object). */
    @NotNull(message = "Thiếu dữ liệu hoạt ảnh .chemx")
    private Object chemx;

    private Integer sortOrder;

    private Boolean isActive;
}
