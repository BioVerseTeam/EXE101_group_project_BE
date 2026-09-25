package com.example.exe101_bioverse.exam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    private Long id;
    private Long classId;          // ID của khối lớp liên kết
    private String name;           // Ví dụ: "Học kỳ 1", "Học kỳ 2"
    private Integer semesterOrder; // 1, 2
    private String description;
}
