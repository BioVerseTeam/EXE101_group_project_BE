package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamHistoryResponse {

    private StudentHistorySummaryResponse summary;

    @Builder.Default
    private List<StudentAttemptSummaryResponse> attempts = new ArrayList<>();
}
