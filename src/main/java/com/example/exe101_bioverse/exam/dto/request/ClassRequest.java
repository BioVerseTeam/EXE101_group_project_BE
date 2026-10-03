package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRequest {
    private Long id;

    @NotBlank(message = "Tên khối lớp không được để trống")
    @Size(max = 100, message = "Tên khối lớp không được vượt quá 100 ký tự")
    private String name;        // Ví dụ: "Lớp 6", "Lớp 7"

    @NotNull(message = "Khối lớp không được để trống")
    @Min(value = 6, message = "Khối lớp phải từ lớp 6 đến lớp 9")
    @Max(value = 9, message = "Khối lớp phải từ lớp 6 đến lớp 9")
    private Integer grade;      // 6, 7, 8, 9

    private String description;
}
