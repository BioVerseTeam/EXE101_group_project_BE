package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentHistorySummaryResponse {

    private Integer totalExamsTaken;

    private Double averageScore;

    private Double highestScore;

    private Long totalTimeSpentSec;

    private Integer totalCorrectQuestions;
}
