package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamPaperResponse {

    private Long examId;

    private String code;

    private String name;

    private String subjectName;

    private String description;

    private Integer durationMinutes;

    private Double totalScore;

    private Integer totalQuestions;

    @Builder.Default
    private List<StudentQuestionPaperResponse> questions = new ArrayList<>();
}
