package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class QuestionRequest {
    private Long id;

    @NotNull(message = "Loại câu hỏi không được để trống")
    private QuestionType type;

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String content;

    @PositiveOrZero(message = "Điểm số không được âm")
    private double point;

    @Min(value = 1, message = "Thứ tự câu hỏi phải bắt đầu từ 1")
    private int questionOrder;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String explain;

    private String description;

    private Long examId;

    @Valid
    private List<AnswerRequest> answers;

    @Valid
    private List<QuestionImageRequest> questionImageRequests;
}
