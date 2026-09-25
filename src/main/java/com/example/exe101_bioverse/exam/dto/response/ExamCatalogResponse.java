package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamCatalogResponse {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Integer duration;
    private Double maxScore;
    private String status;
    private ExamSubjectSummary subject;
    private ExamStatsResponse stats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
