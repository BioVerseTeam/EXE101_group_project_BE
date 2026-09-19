package com.example.exe101_bioverse.streak.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_streaks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStreak {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "current_streak", nullable = false)
    @Builder.Default
    private Integer currentStreak = 0;

    @Column(name = "longest_streak", nullable = false)
    @Builder.Default
    private Integer longestStreak = 0;

    @Column(name = "last_check_in_date")
    private LocalDate lastCheckInDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserStreak newFor(Long userId, LocalDateTime now) {
        return UserStreak.builder()
                .userId(userId)
                .currentStreak(0)
                .longestStreak(0)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Ghi nhận một lần vào app theo ngày lịch (Asia/Ho_Chi_Minh do caller quyết định).
     * Cùng ngày: không đổi chuỗi. Ngày liền trước: +1. Bỏ trống ≥ 1 ngày: reset về 1.
     */
    public void recordVisit(LocalDate today, LocalDateTime now) {
        if (today.equals(lastCheckInDate)) {
            this.updatedAt = now;
            return;
        }

        if (lastCheckInDate != null && lastCheckInDate.equals(today.minusDays(1))) {
            this.currentStreak = this.currentStreak + 1;
        } else {
            this.currentStreak = 1;
        }

        if (this.currentStreak > this.longestStreak) {
            this.longestStreak = this.currentStreak;
        }

        this.lastCheckInDate = today;
        this.updatedAt = now;
    }

    /**
     * Chuỗi còn hiệu lực nếu đã vào hôm nay hoặc hôm qua. Bỏ ≥ 1 ngày đầy đủ thì về 0
     * cho đến khi check-in lại.
     */
    public int effectiveCurrent(LocalDate today) {
        if (lastCheckInDate == null) {
            return 0;
        }
        if (lastCheckInDate.equals(today) || lastCheckInDate.equals(today.minusDays(1))) {
            return currentStreak == null ? 0 : currentStreak;
        }
        return 0;
    }

    public boolean checkedInOn(LocalDate today) {
        return today.equals(lastCheckInDate);
    }
}
