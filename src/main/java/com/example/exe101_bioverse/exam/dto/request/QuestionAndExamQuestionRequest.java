package com.example.exe101_bioverse.exam.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionAndExamQuestionRequest {
    private QuestionRequest questionRequest;
    private ExamQuestionRequest examQuestionRequest;
}
