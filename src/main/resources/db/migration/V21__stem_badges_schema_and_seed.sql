-- ============================================
-- V21: STEM Badges Schema and Seed Data
-- ============================================

CREATE TABLE IF NOT EXISTS stem_badges (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(80) NOT NULL,
    name            VARCHAR(120) NOT NULL,
    icon            VARCHAR(50) NOT NULL,
    category_name   VARCHAR(100) NOT NULL,
    filter_tag      VARCHAR(50) NOT NULL,
    description     TEXT,
    criteria_type   VARCHAR(50) NOT NULL DEFAULT 'ALWAYS_UNLOCKED',
    criteria_value  INT NOT NULL DEFAULT 0,
    reward_xp       INT NOT NULL DEFAULT 50,
    sort_order      INT NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    bg_unlocked     VARCHAR(50) DEFAULT 'bg-[#e8f5e9]',
    border_unlocked VARCHAR(50) DEFAULT 'border-[#2e7d32]',
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_stem_badges_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_stem_badges_active_sort ON stem_badges (is_active, sort_order);
CREATE INDEX IF NOT EXISTS idx_stem_badges_filter_tag ON stem_badges (filter_tag);

-- Seed 12 STEM Scientific Badges
INSERT INTO stem_badges (
    code, name, icon, category_name, filter_tag, description,
    criteria_type, criteria_value, reward_xp, sort_order, is_active,
    bg_unlocked, border_unlocked
) VALUES
-- Nhóm 1: Khởi đầu & Nhập môn (starter)
(
    'badge-starter',
    'Tân Binh BioVerse',
    '🌱',
    'Nhập môn STEM',
    'starter',
    'Đăng ký tài khoản và gia nhập phòng nghiên cứu khoa học BioVerse.',
    'ALWAYS_UNLOCKED',
    0,
    50,
    1,
    TRUE,
    'bg-[#e8f5e9]',
    'border-[#2e7d32]'
),
(
    'badge-profile-ready',
    'Hồ Sơ Nghiên Cứu',
    '🪪',
    'Thông tin cá nhân',
    'starter',
    'Cập nhật đầy đủ họ tên, ngày sinh, số điện thoại và ảnh đại diện.',
    'PROFILE_COMPLETED',
    3,
    50,
    2,
    TRUE,
    'bg-[#e0f2fe]',
    'border-[#0284c7]'
),
-- Nhóm 2: Chuỗi ngày học tập (streak)
(
    'badge-streak-fire',
    'Ngọn Lửa Kiên Trì',
    '🔥',
    'Chuỗi học tập',
    'streak',
    'Duy trì thói quen học tập liên tục từ 3 ngày trở lên.',
    'STREAK_DAYS',
    3,
    100,
    3,
    TRUE,
    'bg-[#ffedd5]',
    'border-[#ea580c]'
),
(
    'badge-streak-week',
    'Chiến Binh 7 Ngày',
    '⚡',
    'Chuỗi học tập',
    'streak',
    'Giữ vững nhịp học suốt 7 ngày trong tuần không gián đoạn.',
    'STREAK_DAYS',
    7,
    150,
    4,
    TRUE,
    'bg-[#f3e8ff]',
    'border-[#9333ea]'
),
(
    'badge-streak-month',
    'Học Giả Bất Bại',
    '🏆',
    'Kỷ lục chuỗi',
    'streak',
    'Chinh phục chuỗi học tập bền bỉ đạt mốc 14 ngày liên tiếp.',
    'STREAK_DAYS',
    14,
    300,
    5,
    TRUE,
    'bg-[#fff9c4]',
    'border-[#ca8a04]'
),
-- Nhóm 3: Thực nghiệm phòng lab 3D (lab)
(
    'badge-microscope',
    'Kính Hiển Vi Vàng',
    '🔬',
    'Thực nghiệm 3D',
    'lab',
    'Khám phá bài học mô hình 3D sinh học đầu tiên trong phòng thí nghiệm.',
    'MODELS_EXPLORED',
    1,
    100,
    6,
    TRUE,
    'bg-[#fff9c4]',
    'border-[#ca8a04]'
),
(
    'badge-anatomy-master',
    'Nhà Giải Phẫu Nhí',
    '🫀',
    'Thực nghiệm 3D',
    'lab',
    'Tương tác và khám phá từ 3 mô hình cơ quan cơ thể người trở lên.',
    'MODELS_EXPLORED',
    3,
    150,
    7,
    TRUE,
    'bg-[#fee2e2]',
    'border-[#dc2626]'
),
(
    'badge-chemistry-lab',
    'Phù Thủy Phản Ứng',
    '🧪',
    'Hóa sinh học',
    'lab',
    'Thực nghiệm tương tác hoạt ảnh phản ứng phân tử hóa học.',
    'REACTION_EXPLORED',
    1,
    120,
    8,
    TRUE,
    'bg-[#f3e8ff]',
    'border-[#7c3aed]'
),
-- Nhóm 4: Cột mốc XP & Cấp độ (xp)
(
    'badge-xp-200',
    'Học Viên Khám Phá',
    '⭐',
    'Cấp độ XP',
    'xp',
    'Tích lũy từ 200 XP qua các bài học và luyện tập trắc nghiệm.',
    'XP_THRESHOLD',
    200,
    50,
    9,
    TRUE,
    'bg-[#e8f5e9]',
    'border-[#2e7d32]'
),
(
    'badge-xp-500',
    'Nhà Di Truyền Học',
    '🧬',
    'Cấp độ XP',
    'xp',
    'Chinh phục cột mốc 500 XP nghiên cứu khoa học STEM.',
    'XP_THRESHOLD',
    500,
    100,
    10,
    TRUE,
    'bg-[#e0f2fe]',
    'border-[#0284c7]'
),
(
    'badge-xp-1000',
    'Chuyên Viên Tài Ba',
    '🌟',
    'Cấp độ XP',
    'xp',
    'Đạt từ 1.000 XP để khẳng định kỹ năng nghiên cứu xuất sắc.',
    'XP_THRESHOLD',
    1000,
    200,
    11,
    TRUE,
    'bg-[#ffedd5]',
    'border-[#ea580c]'
),
(
    'badge-xp-2500',
    'Viện Sĩ BioVerse',
    '👑',
    'Huyền thoại STEM',
    'xp',
    'Danh hiệu danh giá nhất dành cho nhà khoa học trẻ tích lũy trên 2.500 XP.',
    'XP_THRESHOLD',
    2500,
    500,
    12,
    TRUE,
    'bg-[#fee2e2]',
    'border-[#dc2626]'
)
ON CONFLICT (code) DO NOTHING;
