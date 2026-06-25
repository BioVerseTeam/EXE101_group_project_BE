package com.example.exe101_bioverse.dto.response;

import com.example.exe101_bioverse.enums.AnswerType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AnswerResponse {
    private Long id;

    private AnswerType type;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String explain;

    private String description;

    private boolean isCorrect;

    private Long questionId;

    private List<AnswerImageResponse> answerImageResponses;
}
