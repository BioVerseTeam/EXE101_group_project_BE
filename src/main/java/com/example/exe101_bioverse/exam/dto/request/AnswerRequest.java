package com.example.exe101_bioverse.exam.dto.request;

import com.example.exe101_bioverse.exam.enums.AnswerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    private Long id;

    @NotNull(message = "Loại đáp án không được để trống")
    private AnswerType type;

    @NotBlank(message = "Nội dung đáp án không được để trống")
    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String explain;

    private String description;

    @NotNull(message = "Trạng thái đúng/sai không được để trống")
    @com.fasterxml.jackson.annotation.JsonProperty("isCorrect")
    private Boolean isCorrect;

    private Long questionId;

    @Valid
    private List<AnswerImageRequest> answerImageRequests;

    public boolean isCorrect() {
        return Boolean.TRUE.equals(this.isCorrect);
    }
}
