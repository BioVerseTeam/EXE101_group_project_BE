package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionRequest {
    private Long id;

    @PositiveOrZero(message = "Điểm số không được âm")
    private double point;

    @Min(value = 1, message = "Thứ tự câu hỏi phải bắt đầu từ 1")
    private int questionOrder;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @NotNull(message = "ID đề thi không được để trống")
    @Positive(message = "ID đề thi phải lớn hơn 0")
    private Long examId;

    @NotNull(message = "ID câu hỏi không được để trống")
    @Positive(message = "ID câu hỏi phải lớn hơn 0")
    private Long questionId;
}
