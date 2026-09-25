package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.AnswerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttemptAnswerDetailResponse {

    private Long id;

    private AnswerType type;

    private String content;

    @JsonProperty("isCorrect")
    private Boolean isCorrect;

    @JsonProperty("isSelected")
    private Boolean isSelected;

    @Builder.Default
    private List<AnswerImageResponse> images = new ArrayList<>();
}
