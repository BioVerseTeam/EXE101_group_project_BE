package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompositeQuestionResponse {
    private Long examQuestionId;
    private Long questionId;
    private int questionOrder;
    private double point;
    private String content;
    private int answersCount;
}
