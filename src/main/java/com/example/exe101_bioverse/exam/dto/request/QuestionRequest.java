package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionRequest {
    private Long id;

    private QuestionType type;

    private String content;

    private double point;

    private int questionOrder;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String explain;

    private String description;

    private Long examId;

    private List<AnswerRequest> answers;

    private List<QuestionImageRequest> questionImageRequests;
}
