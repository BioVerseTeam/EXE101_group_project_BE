package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    private Long id;

    @NotNull(message = "ID khối lớp không được để trống")
    @Positive(message = "ID khối lớp phải lớn hơn 0")
    private Long classId;          // ID của khối lớp liên kết

    @NotBlank(message = "Tên học kỳ không được để trống")
    @Size(max = 100, message = "Tên học kỳ không được vượt quá 100 ký tự")
    private String name;           // Ví dụ: "Học kỳ 1", "Học kỳ 2"

    @NotNull(message = "Thứ tự học kỳ không được để trống")
    @Min(value = 1, message = "Thứ tự học kỳ phải là 1 hoặc 2")
    @Max(value = 2, message = "Thứ tự học kỳ phải là 1 hoặc 2")
    private Integer semesterOrder; // 1, 2

    private String description;
}
