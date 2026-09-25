package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentQuestionPaperResponse {

    private Long id;

    private Integer questionOrder;

    private QuestionType type;

    private Double point;

    private String content;

    private Long modelAssetId;

    @Builder.Default
    private List<QuestionImageResponse> images = new ArrayList<>();

    @Builder.Default
    private List<StudentAnswerPaperResponse> answers = new ArrayList<>();
}
