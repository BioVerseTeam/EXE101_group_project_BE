package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {
    private String content;
    private QuestionType type;
    private String explain;
    private String description;
}
