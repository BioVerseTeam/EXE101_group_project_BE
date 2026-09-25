package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSubmittedAnswerRequest {

    @NotNull(message = "questionId không được để trống")
    private Long questionId;

    private Long selectedAnswerId;

    private Integer timeSpentSec;
}
