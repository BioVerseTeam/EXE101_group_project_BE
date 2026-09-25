package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Long id;
    private Long semesterId;
    private String semesterName;
    private Long classId;
    private String className;
    private String name;
    private String code;
    private String description;
    private Integer examCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
