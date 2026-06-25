package com.example.exe101_bioverse.dto.request;

import com.example.exe101_bioverse.enums.ExamType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ExamRequest {
    private Long id;

    private String code;

    private ExamType type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String name;

    private String subjectName;

    private String description;

    private List<QuestionRequest> questions;
}