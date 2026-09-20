package com.example.exe101_bioverse.exam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    private Long id;
    private Long semesterId;    // ID của học kỳ liên kết
    private String name;        // Ví dụ: "Khoa học Tự nhiên", "Sinh học"
    private String code;        // Ví dụ: "KHTN6", "BIO7"
    private String description;
}
