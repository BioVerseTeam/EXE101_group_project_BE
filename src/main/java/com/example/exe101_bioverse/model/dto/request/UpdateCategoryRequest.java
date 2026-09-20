package com.example.exe101_bioverse.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @Size(max = 100, message = "Tên loại mẫu không được vượt quá 100 ký tự")
    private String name;

    @Size(max = 50)
    private String subject;

    private Integer sortOrder;

    private Boolean isActive;
}
