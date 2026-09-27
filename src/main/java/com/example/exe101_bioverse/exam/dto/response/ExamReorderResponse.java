package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamReorderResponse {
    private int totalQuestions;
    private double totalPoints;
}
