package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttemptQuestionDetailResponse {

    private Long questionId;

    private Integer questionOrder;

    private String content;

    private QuestionType type;

    private Double point;

    private Double earnedPoint;

    private String explanation;

    private Long selectedAnswerId;

    @JsonProperty("isCorrect")
    private Boolean isCorrect;

    private Long modelAssetId;

    @Builder.Default
    private List<QuestionImageResponse> images = new ArrayList<>();

    @Builder.Default
    private List<StudentAttemptAnswerDetailResponse> answers = new ArrayList<>();
}
