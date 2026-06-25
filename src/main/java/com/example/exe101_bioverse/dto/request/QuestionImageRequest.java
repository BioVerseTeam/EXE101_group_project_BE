package com.example.exe101_bioverse.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionImageRequest {
    private Long id;

    private String name;

    private int displayOrder;

    private LocalDateTime createdDate;

    private String url;

    private Long questionId;
}
