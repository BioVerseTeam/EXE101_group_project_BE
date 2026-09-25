package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.ExamType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamUpdateRequest {

    @Size(max = 255, message = "Mã đề thi không được vượt quá 255 ký tự")
    private String code;

    @Size(max = 255, message = "Tiêu đề đề thi không được vượt quá 255 ký tự")
    private String name;

    private ExamType type;

    private Long subjectId;

    @Size(max = 255, message = "Tên môn học không được vượt quá 255 ký tự")
    private String subjectName;

    private String description;

    private Integer durationMinutes;

    private Double totalScore;

    private Boolean isActive;
}
