package com.example.exe101_bioverse.dto.response;

import com.example.exe101_bioverse.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Long id;

    private QuestionType type;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String explain;

    private String description;

    private Long examId;

    private List<AnswerResponse> answers;

    private List<QuestionImageResponse> questionImageResponses;
}
