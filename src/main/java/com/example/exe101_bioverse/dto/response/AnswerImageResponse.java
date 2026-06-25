package com.example.exe101_bioverse.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnswerImageResponse {
    private Long id;

    private String name;

    private int displayOrder;

    private LocalDateTime createdDate;

    private String url;

    private Long answerId;
}
