package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    private Long id;

    @NotNull(message = "ID học kỳ không được để trống")
    @Positive(message = "ID học kỳ phải lớn hơn 0")
    private Long semesterId;    // ID của học kỳ liên kết

    @NotBlank(message = "Tên môn học không được để trống")
    @Size(max = 100, message = "Tên môn học không được vượt quá 100 ký tự")
    private String name;        // Ví dụ: "Khoa học Tự nhiên", "Sinh học"

    @NotBlank(message = "Mã môn học không được để trống")
    @Size(max = 50, message = "Mã môn học không được vượt quá 50 ký tự")
    private String code;        // Ví dụ: "KHTN6", "BIO7"

    private String description;
}
