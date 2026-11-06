package com.example.exe101_bioverse.feedback.enums;

import lombok.Getter;

@Getter
public enum FeedbackStatus {
    PENDING("Chờ xử lý"),
    REVIEWED("Đã xem"),
    RESOLVED("Đã giải quyết"),
    DISMISSED("Đã bỏ qua");

    private final String displayName;

    FeedbackStatus(String displayName) {
        this.displayName = displayName;
    }
}
