-- ============================================
-- V14: Add featured and metadata to bio_models
-- ============================================

ALTER TABLE bio_models
    ADD COLUMN IF NOT EXISTS slug VARCHAR(255),
    ADD COLUMN IF NOT EXISTS is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS grade INT DEFAULT 8,
    ADD COLUMN IF NOT EXISTS subject VARCHAR(50) DEFAULT 'BIOLOGY',
    ADD COLUMN IF NOT EXISTS badge_text VARCHAR(100),
    ADD COLUMN IF NOT EXISTS action_text VARCHAR(100) DEFAULT 'Khám phá ngay',
    ADD COLUMN IF NOT EXISTS action_icon VARCHAR(50) DEFAULT '3d_rotation',
    ADD COLUMN IF NOT EXISTS target_mode VARCHAR(100),
    ADD COLUMN IF NOT EXISTS views_count BIGINT NOT NULL DEFAULT 0;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uq_bio_models_slug'
    ) THEN
        ALTER TABLE bio_models ADD CONSTRAINT uq_bio_models_slug UNIQUE (slug);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_bio_models_featured ON bio_models(is_featured, sort_order) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_bio_models_grade ON bio_models(grade);
CREATE INDEX IF NOT EXISTS idx_bio_models_slug ON bio_models(slug);

-- ============================================
-- Seed data: 5 Featured Models on Homepage + 5 Catalog Models
-- ============================================

INSERT INTO bio_models (
    name, name_en, slug, category, description,
    grade, subject, badge_text, action_text, action_icon, target_mode,
    model_url, thumbnail_url, is_featured, sort_order, is_active, views_count
) VALUES
(
    'Hệ Thần Kinh & Dây Thần Kinh',
    'Nervous System',
    'he-than-kinh',
    'Hệ thần kinh',
    'Giải phẫu não bộ, tủy sống và mạng lưới dây thần kinh ngoại biên toàn thân.',
    8,
    'BIOLOGY',
    '360° VR',
    'Phẫu tích 3D',
    '3d_rotation',
    'skull',
    '/api/models/skull.glb',
    'https://lh3.googleusercontent.com/aida-public/AB6AXuCx1NA6BR5H8m8OYMOazAdMteKguya7ZcDIqa4goTdehlzBwH4X8BXhfuTWqalZvnItg7t5GzQw-cGaWCYrLtNFuPTF3G4OsinInjayi297Fov-E6FKr0tuh7ogOpj5Uu4fDWxA2-T0hHGR_rkp4jYW7VLC-MoECRh0corcqVNSwqlJJcqxVjH8kjB-bMvDD1NlejPXrcfOPvjMEQ4Ty-D0H8bMFJzSOTMYQDqpJ2W6D98bzXn-XS1izSGD3iNzAVtQ0rU',
    TRUE,
    1,
    TRUE,
    1280
),
(
    'Hệ Tiêu Hóa & Nội Tạng',
    'Digestive System',
    'he-tieu-hoa',
    'Hệ tiêu hóa',
    'Mô phỏng đường tiêu hóa từ thực quản, dạ dày, gan mật đến đường ruột.',
    8,
    'BIOLOGY',
    'Tương tác',
    'Khám phá ngay',
    'visibility',
    'organs',
    '/api/models/realistic_human_lungs.glb',
    'https://lh3.googleusercontent.com/aida-public/AB6AXuBGmeihJGhi_1k90xVq_cvzr4Qw4caStqW_otZAr1cGklp5OnEb8GSPzI3wqosQF6KFEVVK1YIF1x7AvRvSlzi6nQIr43twiJQA7NDRuMtiyAQreOk45sVn_nZ88WASKb-_pIXXScW5nZ7UWgxxNk7EJIYhF6dnGfcHHXzI6XPOgR3wzW6HKoLS_cpzD9PAQEcux3Jo5FIvEifp-3VtaFwgFE0AULzP2SQSe9EILxeVm-gW2BRm9omVX7W2mUcrbIV_yw0',
    TRUE,
    2,
    TRUE,
    1050
),
(
    'Hệ Tuần Hoàn & Tim Mạch',
    'Circulatory System & Human Heart',
    'he-tuan-hoan',
    'Hệ tuần hoàn',
    'Dòng chảy máu qua tim, động mạch lớn và mao mạch vận chuyển khí O2.',
    8,
    'BIOLOGY',
    'Co bóp 75 BPM',
    'Xem nhịp tim',
    'vital_signs',
    'organs',
    '/api/models/human_heart_3d.glb',
    'https://lh3.googleusercontent.com/aida-public/AB6AXuCC-LX89ypbBkMHTZlUZ7etrRMwTYXsXjQpE4YDrRWlLSkCjlKSreI7NlLGso7KvT4FVU_tLaRQagBAtLc_hmX0p9xYdNRTNuuuPEWGHX34APO_jAQnQK2feh5xfObphzuypOZPnmmvDe-4eTffswsCgusrH06JEkyPvkjMfU-wlUCpeEgg_vclBikGdNad5OWEuZj_Qsk162pheB8UF6JXqdkOg4R7LDbmr1WS_aA2ufhWtPr2OL7sww9d7DD8-VBgS0k',
    TRUE,
    3,
    TRUE,
    2140
),
(
    'Hệ Xương & Khung Xương Người',
    'Skeletal System',
    'he-xuong',
    'Hệ vận động',
    'Khảo sát cấu trúc sọ, lồng ngực, cột sống và các khớp cử động linh hoạt.',
    8,
    'BIOLOGY',
    '206 Xương',
    'Khám phá xương',
    'view_in_ar',
    'skull',
    '/api/models/skull.glb',
    'https://lh3.googleusercontent.com/aida-public/AB6AXuCuKv4MbLt7E2DocsEGVE9dCgPrJw19-W-4kVR7eEBbloaV9hKlhed14pG8UZThG11rCKkdO1FBUl18v4Lomq5G8AXIlmPIaUkRhlmoDLzk2blgzfHHTW1WFh1gDmZPgLb-pQ8FaanHi33w2eE44BGk3bUFCDpkPtAKyUmyTkLHWKc3LOoMejBeQKqHW-xLM2_qmAT0lyhtzpL2Lcqnas6CLlf-YtA4atRGcBART0tgEarpVLl0930rrYii-3cQF4ZrtMY',
    TRUE,
    4,
    TRUE,
    1890
),
(
    'Hệ Cơ Bắp Toàn Thân',
    'Muscular System',
    'he-co-bap',
    'Hệ vận động',
    'Mô phỏng co giãn cơ vân, cơ trơn và cơ tim khi cơ thể vận động.',
    8,
    'BIOLOGY',
    'Giải phẫu cơ',
    'Soi mô cơ',
    'biotech',
    'paramecium',
    '/api/models/trung_giay.glb',
    'https://lh3.googleusercontent.com/aida-public/AB6AXuCPOHLaSQitnfZ14g2Ju8hKuB2PoLddolFvuQahUEsZYOGyQbKUNHtpEclV-m0sSYxeC2e0spR9JPAs-WlvW46EbKA0DnfNQj5__l52UM1UkLMO8QLGmgjVDuGMBa_02_qbDnIQfQ7Go3XrxL31BxiOeMf_DGfK1Cfl1aDOHniBT-HlMncVJ8sOOZb_c1U_Xe5t7YsIv-Z5y7CMaPHq0IROwRVEqsUL7ATiOuyDUK-imAgu33LKW4CGL82P0VIqpfGJUfM',
    TRUE,
    5,
    TRUE,
    1420
),
(
    'Trùng Đế Giày (Paramecium)',
    'Paramecium Caudatum',
    'trung-de-giay',
    'Động vật nguyên sinh',
    'Cấu trúc đơn bào động vật nguyên sinh với rãnh miệng, không bào co bóp và lông bơi.',
    6,
    'BIOLOGY',
    'Đơn bào 3D',
    'Soi kính hiển vi',
    'biotech',
    'paramecium',
    '/api/models/trung_giay.glb',
    'https://images.unsplash.com/photo-1530026405186-ed1f139313f8?w=800&auto=format&fit=crop&q=60',
    FALSE,
    6,
    TRUE,
    950
),
(
    'Phổi & Hệ Hô Hấp Người',
    'Respiratory System & Lungs',
    'phoi-nguoi',
    'Hệ hô hấp',
    'Cấu trúc phế nang, khí quản và quá trình trao đổi khí oxy / carbon dioxide ở phổi.',
    8,
    'BIOLOGY',
    'Trao đổi khí',
    'Quan sát phổi',
    'air',
    'organs',
    '/api/models/realistic_human_lungs.glb',
    'https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=800&auto=format&fit=crop&q=60',
    FALSE,
    7,
    TRUE,
    870
),
(
    'Quá Trình Phân Bào Nguyên Phân (Mitosis)',
    'Mitosis Cell Division',
    'nguyen-phan-te-bao',
    'Sinh học tế bào',
    'Mô phỏng 4 kỳ nguyên phân: Kỳ đầu, kỳ giữa, kỳ sau và kỳ cuối ở tế bào nhân thực.',
    9,
    'BIOLOGY',
    '4 Kỳ Phân Bào',
    'Chạy mô phỏng',
    'play_circle',
    'mitosis',
    '/api/models/mitosis_animation.glb',
    'https://images.unsplash.com/photo-1507668077129-56e32842fceb?w=800&auto=format&fit=crop&q=60',
    FALSE,
    8,
    TRUE,
    740
),
(
    'Bộ Dụng Cụ Hóa Học & Bình Định Mức',
    'Chemistry Glassware & Flasks',
    'dung-cu-hoa-hoc',
    'Hóa học thực nghiệm',
    'Dụng cụ thí nghiệm tiêu chuẩn gồm bình nón (Erlenmeyer), cốc đong và giá đỡ phòng lab.',
    8,
    'CHEMISTRY',
    'Dụng cụ Lab',
    'Vào phòng Lab',
    'science',
    'chemistry',
    '/api/models/lab_flask.glb',
    'https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?w=800&auto=format&fit=crop&q=60',
    FALSE,
    9,
    TRUE,
    630
),
(
    'Ống Nghiệm Thủy Tinh & Thuốc Thử Phenolphthalein',
    'Test Tube & Phenolphthalein Indicator',
    'ong-nghiem-thuoc-thu',
    'Hóa học thực nghiệm',
    'Chỉ thị màu axit - bazơ, quan sát đổi màu phenolphthalein từ không màu sang hồng cánh sen.',
    8,
    'CHEMISTRY',
    'Chỉ thị màu',
    'Thử axit/bazơ',
    'science',
    'chemistry',
    '/api/models/phenol_bottle.glb',
    'https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=800&auto=format&fit=crop&q=60',
    FALSE,
    10,
    TRUE,
    520
)
ON CONFLICT (slug) DO UPDATE SET
    name = EXCLUDED.name,
    name_en = EXCLUDED.name_en,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    grade = EXCLUDED.grade,
    subject = EXCLUDED.subject,
    badge_text = EXCLUDED.badge_text,
    action_text = EXCLUDED.action_text,
    action_icon = EXCLUDED.action_icon,
    target_mode = EXCLUDED.target_mode,
    model_url = EXCLUDED.model_url,
    thumbnail_url = EXCLUDED.thumbnail_url,
    is_featured = EXCLUDED.is_featured,
    sort_order = EXCLUDED.sort_order,
    is_active = EXCLUDED.is_active;
