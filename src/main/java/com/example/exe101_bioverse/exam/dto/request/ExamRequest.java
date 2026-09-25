package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.ExamType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private Long id;

    @NotBlank(message = "Mã đề thi không được để trống")
    @Size(max = 255, message = "Mã đề thi không được vượt quá 255 ký tự")
    private String code;

    @NotNull(message = "Loại đề thi không được để trống")
    private ExamType type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @NotBlank(message = "Tiêu đề đề thi không được để trống")
    @Size(max = 255, message = "Tiêu đề đề thi không được vượt quá 255 ký tự")
    private String name;

    @NotBlank(message = "Tên môn học không được để trống")
    @Size(max = 255, message = "Tên môn học không được vượt quá 255 ký tự")
    private String subjectName;

    private String description;

    @Valid
    private List<QuestionRequest> questions;
}