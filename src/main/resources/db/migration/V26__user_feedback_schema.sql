-- ============================================
-- V26: User Feedback & Management Schema
-- Cho phép người dùng gửi ý kiến đóng góp, báo lỗi, đánh giá mô hình/bài học
-- và ban quản trị (Admin) theo dõi, lọc, xử lý và xuất báo cáo.
-- ============================================

CREATE TABLE IF NOT EXISTS feedbacks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    user_email VARCHAR(255),
    user_name VARCHAR(255),
    category VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    rating INT DEFAULT 5,
    title VARCHAR(255),
    content TEXT NOT NULL,
    page_url VARCHAR(500),
    device_info VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    responded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_feedbacks_created_at ON feedbacks (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_feedbacks_status ON feedbacks (status);
CREATE INDEX IF NOT EXISTS idx_feedbacks_category ON feedbacks (category);
CREATE INDEX IF NOT EXISTS idx_feedbacks_user_id ON feedbacks (user_id);

-- Dữ liệu mẫu (Seed) ban đầu để phòng điều hành quản trị có sẵn dữ liệu test và xuất Excel
INSERT INTO feedbacks (user_id, user_email, user_name, category, rating, title, content, page_url, device_info, status, admin_notes, responded_at, created_at, updated_at)
VALUES
(
    NULL,
    'hocsinh.khtn8@gmail.com',
    'Nguyễn Văn An (Lớp 8A)',
    'CONTENT',
    5,
    'Mô hình tế bào thực vật rất trực quan',
    'Em rất thích phần phóng to lục lạp và không bào trong bài Sinh học 8. Mô hình 3D xoay mượt mà và chú thích rõ ràng. Mong nhóm phát triển thêm các mô hình vi khuẩn đường ruột nữa ạ!',
    '/sinh-hoc',
    'Chrome 128 / macOS / 1440x900',
    'RESOLVED',
    'Đã ghi nhận đề xuất thêm mô hình vi khuẩn đường ruột vào lộ trình môn Sinh học kỳ 2.',
    NOW() - INTERVAL '1 DAY',
    NOW() - INTERVAL '3 DAY',
    NOW() - INTERVAL '1 DAY'
),
(
    NULL,
    'thaygiao.ly7@edu.vn',
    'Thầy Trần Minh Hoàng',
    'FEATURE',
    5,
    'Đề xuất bổ sung công cụ đo khoảng cách và góc trong mô hình Vật lý',
    'Phần đòn bẩy và quang học rất sinh động. Nếu có thêm thước đo milimet hoặc đo góc khúc xạ ánh sáng ngay trên nền 3D thì việc giảng dạy cho học sinh trên lớp sẽ trực quan hơn rất nhiều.',
    '/vat-ly',
    'Edge 129 / Windows 11 / 1920x1080',
    'REVIEWED',
    'Đang trao đổi với team đồ họa Three.js để tích hợp thước đo góc và thước kẻ đo lực.',
    NOW() - INTERVAL '12 HOUR',
    NOW() - INTERVAL '2 DAY',
    NOW() - INTERVAL '12 HOUR'
),
(
    NULL,
    'lanhuong.stem@outlook.com',
    'Trần Thị Lan Hương',
    'BUG',
    3,
    'Lỗi giật nhẹ khi xoay mô hình sọ người trên điện thoại',
    'Khi xem mô hình hộp sọ trên Safari iPhone 13, đôi lúc mô hình bị khựng khoảng 1 giây khi phóng to hết cỡ lớp xương thái dương.',
    '/mo-hinh?id=skull',
    'Mobile Safari / iOS 17.5 / iPhone 13 (390x844)',
    'PENDING',
    NULL,
    NULL,
    NOW() - INTERVAL '1 DAY',
    NOW() - INTERVAL '1 DAY'
),
(
    NULL,
    'quanghuy.ptnk@gmail.com',
    'Lê Quang Huy',
    'UI_UX',
    4,
    'Giao diện phòng thí nghiệm rất đẹp nhưng cần phím tắt',
    'Phong cách sketchbook neo-brutalist rất cá tính và cuốn hút! Nếu có thêm phím tắt như Space để tạm dừng phản ứng hóa học hoặc R để reset góc quay thì trải nghiệm sẽ tuyệt vời hơn nữa.',
    '/phan-ung',
    'Chrome 129 / Windows 10 / 1920x1080',
    'PENDING',
    NULL,
    NULL,
    NOW() - INTERVAL '6 HOUR',
    NOW() - INTERVAL '6 HOUR'
),
(
    NULL,
    'phuhuynh.khtn@gmail.com',
    'Bác Mai Đức Thuận',
    'GENERAL',
    5,
    'Phần mềm hỗ trợ học tập khoa học rất ý nghĩa',
    'Con tôi học lớp 7 rất hào hứng mỗi tối khi học bài về phản ứng hóa học và các mẫu sinh học. Xin cảm ơn đội ngũ phát triển BioVerse!',
    '/',
    'Chrome 128 / Android 14 / Tablet (800x1280)',
    'RESOLVED',
    'Gửi lời cảm ơn chân thành đến phụ huynh và học sinh.',
    NOW() - INTERVAL '2 HOUR',
    NOW() - INTERVAL '5 HOUR',
    NOW() - INTERVAL '2 HOUR'
);
