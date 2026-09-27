package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamStatsResponse {
    private int questionCount;
    private double totalAssignedPoints;
    private int participantCount;
    private double averageScore;
}
