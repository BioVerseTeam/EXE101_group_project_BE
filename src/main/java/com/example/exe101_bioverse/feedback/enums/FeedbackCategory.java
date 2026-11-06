package com.example.exe101_bioverse.feedback.enums;

import lombok.Getter;

@Getter
public enum FeedbackCategory {
    BUG("Báo lỗi kỹ thuật"),
    CONTENT("Nội dung bài học & 3D"),
    UI_UX("Giao diện & Trải nghiệm"),
    FEATURE("Đề xuất tính năng"),
    GENERAL("Góp ý chung");

    private final String displayName;

    FeedbackCategory(String displayName) {
        this.displayName = displayName;
    }
}
