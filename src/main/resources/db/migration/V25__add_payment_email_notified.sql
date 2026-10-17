-- ============================================
-- V25: Add email_notified_at to payments table
-- Để đánh dấu thời điểm đã gửi email thông báo kết quả thanh toán cho người dùng (tránh gửi trùng lặp)
-- ============================================

ALTER TABLE payments ADD COLUMN IF NOT EXISTS email_notified_at TIMESTAMP;
