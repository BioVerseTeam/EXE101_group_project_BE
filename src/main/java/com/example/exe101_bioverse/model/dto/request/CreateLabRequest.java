package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabRequest {

    @NotBlank(message = "Tên lab không được để trống")
    @Size(max = 100, message = "Tên lab không được vượt quá 100 ký tự")
    private String name;

    @Size(max = 100, message = "Mã lab không được vượt quá 100 ký tự")
    private String code;

    @Size(max = 500)
    private String description;

    private Integer sortOrder;

    private Boolean isActive;
}
