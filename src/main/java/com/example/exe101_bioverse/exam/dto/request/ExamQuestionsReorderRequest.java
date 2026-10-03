package com.example.exe101_bioverse.exam.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ExamQuestionsReorderRequest {

    @NotEmpty(message = "Danh sách sắp xếp không được để trống")
    @Valid
    private List<ReorderItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReorderItem {
        @NotNull(message = "ID liên kết đề thi - câu hỏi không được để trống")
        private Long examQuestionId;

        @Min(value = 1, message = "Thứ tự câu hỏi phải từ 1 trở lên")
        private int newOrder;

        @PositiveOrZero(message = "Điểm số không được âm")
        private double point;
    }
}
