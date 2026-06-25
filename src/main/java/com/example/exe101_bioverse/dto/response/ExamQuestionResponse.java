package com.example.exe101_bioverse.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamQuestionResponse {
    private Long id;

    private double point;

    private int questionOrder;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Long examId;

    private Long questionId;
}
