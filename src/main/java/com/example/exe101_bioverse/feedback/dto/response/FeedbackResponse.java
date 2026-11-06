package com.example.exe101_bioverse.feedback.dto.response;

import com.example.exe101_bioverse.feedback.entity.Feedback;
import com.example.exe101_bioverse.feedback.enums.FeedbackCategory;
import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private String userAvatar;
    private FeedbackCategory category;
    private String categoryDisplayName;
    private Integer rating;
    private String title;
    private String content;
    private String pageUrl;
    private String deviceInfo;
    private FeedbackStatus status;
    private String statusDisplayName;
    private String adminNotes;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FeedbackResponse from(Feedback feedback) {
        if (feedback == null) return null;

        Long uid = null;
        String email = feedback.getUserEmail();
        String name = feedback.getUserName();
        String avatar = null;

        if (feedback.getUser() != null) {
            uid = feedback.getUser().getId();
            if (email == null || email.isBlank()) {
                email = feedback.getUser().getEmail();
            }
            if (name == null || name.isBlank()) {
                name = feedback.getUser().getFullName();
            }
            avatar = feedback.getUser().getAvatarUrl();
        }

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .userId(uid)
                .userEmail(email)
                .userName(name)
                .userAvatar(avatar)
                .category(feedback.getCategory())
                .categoryDisplayName(feedback.getCategory() != null ? feedback.getCategory().getDisplayName() : null)
                .rating(feedback.getRating())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .pageUrl(feedback.getPageUrl())
                .deviceInfo(feedback.getDeviceInfo())
                .status(feedback.getStatus())
                .statusDisplayName(feedback.getStatus() != null ? feedback.getStatus().getDisplayName() : null)
                .adminNotes(feedback.getAdminNotes())
                .respondedAt(feedback.getRespondedAt())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
    }
}
