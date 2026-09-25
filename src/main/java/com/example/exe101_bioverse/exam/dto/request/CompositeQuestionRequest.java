package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CompositeQuestionRequest {

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String content;

    @PositiveOrZero(message = "Điểm số không được âm")
    @Builder.Default
    private double point = 0.25;

    @Builder.Default
    private QuestionType type = QuestionType.SINGLE_CHOICE;

    private String difficultyLevel;

    private String topic;

    private String explanation;

    @Valid
    private List<CompositeImageRequest> images;

    @NotEmpty(message = "Danh sách đáp án không được để trống")
    @Valid
    private List<CompositeAnswerRequest> answers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompositeAnswerRequest {
        @NotBlank(message = "Nội dung đáp án không được để trống")
        private String content;

        @com.fasterxml.jackson.annotation.JsonProperty("isCorrect")
        private Boolean isCorrect;

        private String explanation;

        public boolean isCorrect() {
            return Boolean.TRUE.equals(this.isCorrect);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompositeImageRequest {
        @NotBlank(message = "URL hình ảnh không được để trống")
        private String imageUrl;

        private String caption;
    }
}
