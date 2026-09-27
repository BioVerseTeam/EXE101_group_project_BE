package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamAttemptDetailResponse {

    private Long attemptId;

    private Long examId;

    private String examCode;

    private String examTitle;

    private Double score;

    private Integer totalQuestions;

    private Integer correctCount;

    private Integer timeSpentSec;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    @Builder.Default
    private List<StudentAttemptQuestionDetailResponse> questions = new ArrayList<>();
}
