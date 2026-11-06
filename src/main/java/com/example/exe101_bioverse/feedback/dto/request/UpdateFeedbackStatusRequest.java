package com.example.exe101_bioverse.feedback.dto.request;

import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedbackStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private FeedbackStatus status;

    private String adminNotes;
}
