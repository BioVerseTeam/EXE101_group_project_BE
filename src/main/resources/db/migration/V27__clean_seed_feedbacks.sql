-- ============================================
-- V27: Remove Seed Feedbacks
-- Dọn sạch toàn bộ dữ liệu mẫu ban đầu để bảng feedbacks hoàn toàn trống
-- ============================================

DELETE FROM feedbacks WHERE user_email IN (
    'hocsinh.khtn8@gmail.com',
    'thaygiao.ly7@edu.vn',
    'lanhuong.stem@outlook.com',
    'quanghuy.ptnk@gmail.com',
    'phuhuynh.khtn@gmail.com'
);

-- Reset lại bộ đếm ID nếu bảng không còn bản ghi nào
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM feedbacks) THEN
        ALTER SEQUENCE feedbacks_id_seq RESTART WITH 1;
    END IF;
END $$;
