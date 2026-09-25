package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamSubmitRequest {

    private LocalDateTime startedAt;

    private Integer timeSpentSec;

    @NotNull(message = "Danh sách câu trả lời không được để trống")
    @Valid
    @Builder.Default
    private List<StudentSubmittedAnswerRequest> answers = new ArrayList<>();
}
