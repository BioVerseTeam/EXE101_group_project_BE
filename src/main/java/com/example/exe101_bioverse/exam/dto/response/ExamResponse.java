package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long id;

    private String code;

    private ExamType type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String name;

    private String subjectName;

    private String description;

    private List<QuestionResponse> questions;


}