package com.example.exe101_bioverse.exam.dto.request;

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
public class ExamDuplicateRequest {

    @NotBlank(message = "Mã đề thi mới không được để trống")
    @Size(max = 255, message = "Mã đề thi mới không được vượt quá 255 ký tự")
    private String newCode;

    @NotBlank(message = "Tiêu đề đề thi mới không được để trống")
    @Size(max = 255, message = "Tiêu đề đề thi mới không được vượt quá 255 ký tự")
    private String newTitle;
}
