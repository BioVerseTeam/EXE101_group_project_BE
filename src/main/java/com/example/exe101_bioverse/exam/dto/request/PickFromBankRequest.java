package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickFromBankRequest {

    @NotEmpty(message = "Danh sách ID câu hỏi không được để trống")
    private List<Long> questionIds;

    @PositiveOrZero(message = "Điểm số mặc định không được âm")
    @Builder.Default
    private double defaultPoint = 0.25;
}
