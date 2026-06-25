package com.example.exe101_bioverse.exam.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionImageResponse {
    private Long id;

    private String name;

    private int displayOrder;

    private LocalDateTime createdDate;

    private String url;

    private Long questionId;
}
