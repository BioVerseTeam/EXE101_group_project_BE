package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSummaryResponse {
    private long totalExams;
    private Map<String, Long> gradeCounts;
    private Map<String, Long> typeCounts;
    private List<String> subjects;
}
