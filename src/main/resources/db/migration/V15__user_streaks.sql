-- ============================================
-- V15: Chuỗi ngày học (daily streak)
-- Mỗi ngày user vào app = +1. Bỏ 1 ngày = mất chuỗi.
-- ============================================

CREATE TABLE IF NOT EXISTS user_streaks (
    user_id             BIGINT PRIMARY KEY,
    current_streak      INT NOT NULL DEFAULT 0,
    longest_streak      INT NOT NULL DEFAULT 0,
    last_check_in_date  DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_user_streaks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_user_streaks_current CHECK (current_streak >= 0),
    CONSTRAINT chk_user_streaks_longest CHECK (longest_streak >= 0)
);

CREATE INDEX IF NOT EXISTS idx_user_streaks_last_check_in ON user_streaks(last_check_in_date);
