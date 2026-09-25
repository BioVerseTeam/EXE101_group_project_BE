package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerImageRequest {
    private Long id;

    private String name;

    @Min(value = 1, message = "Thứ tự hiển thị ảnh phải bắt đầu từ 1")
    private int displayOrder;

    private LocalDateTime createdDate;

    @NotBlank(message = "URL hình ảnh không được để trống")
    private String url;

    private Long answerId;
}
