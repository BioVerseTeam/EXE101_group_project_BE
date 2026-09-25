package com.example.exe101_bioverse.exam.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterResponse {
    private Long id;
    private Long classId;
    private String className;
    private Integer classGrade;
    private String name;
    private Integer semesterOrder;
    private String description;
    private Integer subjectCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
