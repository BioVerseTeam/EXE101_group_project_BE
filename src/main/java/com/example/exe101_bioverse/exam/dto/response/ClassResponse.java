package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassResponse {
    private Long id;
    private String name;
    private Integer grade;
    private String description;
    private Integer semesterCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
