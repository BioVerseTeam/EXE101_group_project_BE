package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttemptSummaryResponse {

    private Long attemptId;

    private Long examId;

    private String examCode;

    private String examTitle;

    private String subjectName;

    private Double score;

    private Integer totalQuestions;

    private Integer correctCount;

    private Integer timeSpentSec;

    private LocalDateTime submittedAt;
}
