-- ============================================
-- V8: Biology 3D Models Schema
-- (Mô hình 3D sinh vật - click hiển thị thông tin)
-- ============================================

-- ============================================
-- BIO MODELS (Mô hình 3D sinh vật)
-- ============================================
CREATE TABLE IF NOT EXISTS bio_models (
    id                  BIGSERIAL PRIMARY KEY,
    lesson_id           BIGINT,                    -- nullable: có thể không gắn bài cụ thể
    name                VARCHAR(255) NOT NULL,     -- 'Trùng đế giày (Paramecium)'
    name_en             VARCHAR(255),              -- 'Paramecium'
    scientific_name     VARCHAR(255),              -- 'Paramecium caudatum'
    category            VARCHAR(100),              -- 'Động vật nguyên sinh', 'Thực vật', 'Vi sinh vật'
    -- Thông tin hiển thị khi click vào mô hình
    description         TEXT,
    habitat             TEXT,                      -- Môi trường sống
    characteristics     TEXT,                      -- Đặc điểm nổi bật
    classification      JSONB,                     -- { "kingdom": "Protista", "phylum": "Ciliophora", ... }
    fun_facts           JSONB,                     -- ["Có thể tự sinh sản...", "Sống trong nước ngọt..."]
    -- 3D asset files
    model_url           VARCHAR(500),              -- URL .glb/.gltf file
    thumbnail_url       VARCHAR(500),
    model_format        VARCHAR(20) DEFAULT 'glb',
    model_size_bytes    BIGINT,
    -- 3D display settings (camera, scale, rotation mặc định)
    default_scale       DECIMAL(6,3) DEFAULT 1.0,
    default_rotation    JSONB,                     -- { "x": 0, "y": 0, "z": 0 }
    camera_position     JSONB,                     -- { "x": 0, "y": 2, "z": 5 }
    -- Annotations (label các bộ phận khi click)
    -- Ví dụ: [{ "label": "Lông bơi (cilia)", "position": [1.2, 0.5, 0], "description": "Dùng để di chuyển..." }]
    annotations         JSONB,
    --
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order          INT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_bio_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

CREATE INDEX idx_bio_category ON bio_models(category);
CREATE INDEX idx_bio_lesson ON bio_models(lesson_id);
