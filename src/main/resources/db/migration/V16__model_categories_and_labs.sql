-- ============================================
-- V16: Admin-managed specimen categories and labs
-- ============================================

CREATE TABLE IF NOT EXISTS bio_model_categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(120) NOT NULL,
    subject     VARCHAR(50),
    sort_order  INT NOT NULL DEFAULT 0,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_bio_model_categories_name UNIQUE (name),
    CONSTRAINT uq_bio_model_categories_slug UNIQUE (slug)
);

CREATE TABLE IF NOT EXISTS bio_labs (
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(100) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    is_system   BOOLEAN NOT NULL DEFAULT FALSE,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_bio_labs_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_bio_model_categories_active ON bio_model_categories (is_active, sort_order);
CREATE INDEX IF NOT EXISTS idx_bio_labs_active ON bio_labs (is_active, sort_order);

INSERT INTO bio_model_categories (name, slug, sort_order) VALUES
    ('Hệ thần kinh', 'he-than-kinh', 1),
    ('Hệ tuần hoàn', 'he-tuan-hoan', 2),
    ('Hệ hô hấp', 'he-ho-hap', 3),
    ('Hệ tiêu hóa', 'he-tieu-hoa', 4),
    ('Giải phẫu', 'giai-phau', 5),
    ('Cơ quan nội tạng', 'co-quan-noi-tang', 6),
    ('Tế bào', 'te-bao', 7),
    ('Phân bào', 'phan-bao', 8),
    ('Thực vật', 'thuc-vat', 9),
    ('Vi sinh vật', 'vi-sinh-vat', 10)
ON CONFLICT (name) DO NOTHING;

INSERT INTO bio_model_categories (name, slug, sort_order)
SELECT DISTINCT ON (LOWER(TRIM(m.category)))
    TRIM(m.category),
    LEFT('imported-' || MD5(LOWER(TRIM(m.category))), 120),
    50
FROM bio_models m
WHERE m.category IS NOT NULL
  AND TRIM(m.category) <> ''
  AND NOT EXISTS (
      SELECT 1
      FROM bio_model_categories c
      WHERE LOWER(c.name) = LOWER(TRIM(m.category))
  )
ORDER BY LOWER(TRIM(m.category));

INSERT INTO bio_labs (code, name, description, is_system, sort_order) VALUES
    ('skull', 'Hộp sọ', 'Viewer xương sọ 3D — click vùng xương, đổi góc camera.', TRUE, 1),
    ('organs', 'Cơ quan / Tim', 'Viewer cơ quan nội tạng và tim.', TRUE, 2),
    ('paramecium', 'Trùng giày', 'Viewer trùng giày (Paramecium).', TRUE, 3),
    ('plant', 'Thực vật', 'Viewer cấu tạo thực vật.', TRUE, 4),
    ('mitosis', 'Phân bào', 'Mô phỏng các pha phân bào.', TRUE, 5),
    ('chemistry', 'Hóa học', 'Phòng thí nghiệm hóa — hóa chất và phản ứng.', TRUE, 6)
ON CONFLICT (code) DO NOTHING;

INSERT INTO bio_labs (code, name, description, is_system, sort_order)
SELECT DISTINCT ON (LOWER(TRIM(m.target_mode)))
    LOWER(TRIM(m.target_mode)),
    INITCAP(REPLACE(TRIM(m.target_mode), '-', ' ')),
    'Lab được nhập từ nhân mẫu sẵn có.',
    FALSE,
    50
FROM bio_models m
WHERE m.target_mode IS NOT NULL
  AND TRIM(m.target_mode) <> ''
  AND NOT EXISTS (
      SELECT 1
      FROM bio_labs l
      WHERE LOWER(l.code) = LOWER(TRIM(m.target_mode))
  )
ORDER BY LOWER(TRIM(m.target_mode));
