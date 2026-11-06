package com.example.exe101_bioverse.feedback.controller;

import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.feedback.dto.request.CreateFeedbackRequest;
import com.example.exe101_bioverse.feedback.dto.response.FeedbackResponse;
import com.example.exe101_bioverse.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "User Feedback", description = "Tiếp nhận ý kiến đóng góp, đánh giá và báo lỗi từ người dùng")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Gửi ý kiến đóng góp, đánh giá hoặc báo lỗi từ học sinh, giáo viên hoặc khách")
    public ResponseEntity<ApiResponse<FeedbackResponse>> submitFeedback(
            @Valid @RequestBody CreateFeedbackRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal != null ? principal.getId() : null;
        FeedbackResponse response = feedbackService.createFeedback(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Cảm ơn bạn đã gửi ý kiến đóng góp cho BioVerse!"));
    }
}
