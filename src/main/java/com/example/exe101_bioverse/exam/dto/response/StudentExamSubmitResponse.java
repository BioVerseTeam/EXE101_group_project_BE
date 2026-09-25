package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamSubmitResponse {

    private Long attemptId;

    private Long examId;

    private String examTitle;

    private Double score;

    private Integer totalQuestions;

    private Integer correctCount;

    private Integer timeSpentSec;

    private Integer earnedXp;

    private LocalDateTime submittedAt;

    private String feedback;
}
