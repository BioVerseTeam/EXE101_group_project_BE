package com.example.exe101_bioverse.exam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRequest {
    private Long id;
    private String name;        // Ví dụ: "Lớp 6", "Lớp 7"
    private Integer grade;      // 6, 7, 8, 9
    private String description;
}
