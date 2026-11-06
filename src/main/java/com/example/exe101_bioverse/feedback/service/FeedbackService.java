package com.example.exe101_bioverse.feedback.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.feedback.dto.request.CreateFeedbackRequest;
import com.example.exe101_bioverse.feedback.dto.request.UpdateFeedbackStatusRequest;
import com.example.exe101_bioverse.feedback.dto.response.FeedbackResponse;
import com.example.exe101_bioverse.feedback.enums.FeedbackCategory;
import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    FeedbackResponse createFeedback(CreateFeedbackRequest request, Long currentUserId);

    PageResponse<FeedbackResponse> searchFeedbacks(
            FeedbackStatus status,
            FeedbackCategory category,
            Integer rating,
            String keyword,
            Pageable pageable);

    FeedbackResponse getFeedback(Long id);

    FeedbackResponse updateStatus(Long id, UpdateFeedbackStatusRequest request);

    void deleteFeedback(Long id);

    byte[] exportToCsv(
            FeedbackStatus status,
            FeedbackCategory category,
            Integer rating,
            String keyword);
}
