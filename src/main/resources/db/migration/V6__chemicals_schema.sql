-- ============================================
-- V6: Chemicals Library Schema
-- (Thư viện chất hoá học + 3D assets)
-- ============================================

-- ============================================
-- CHEMICALS (Bảng chất hoá học)
-- Click vào 1 chất -> hiển thị thông tin + mô hình 3D
-- ============================================
CREATE TABLE IF NOT EXISTS chemicals (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,         -- 'Hydrochloric acid'
    name_vi             VARCHAR(255) NOT NULL,         -- 'Axit clohiđric'
    formula             VARCHAR(100) NOT NULL,         -- 'HCl'
    molecular_weight    DECIMAL(10,4),                 -- 36.461
    state               VARCHAR(50),                   -- 'liquid', 'solid', 'gas', 'solid_powder'
    color               VARCHAR(50),                   -- hex color '#3fa9d6' hoặc tên 'transparent'
    density             DECIMAL(10,4),
    melting_point       DECIMAL(10,2),                 -- °C
    boiling_point       DECIMAL(10,2),                 -- °C
    -- Safety
    hazard_level        VARCHAR(50),                   -- 'none', 'low', 'medium', 'high'
    hazard_description  TEXT,
    -- Descriptive
    description         TEXT,
    properties          JSONB,                         -- mở rộng: tính chất lý/hoá khác
    -- 3D visual
    default_3d_color    VARCHAR(7),                    -- hex for 3D render
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_chemicals_formula ON chemicals(formula);
CREATE INDEX idx_chemicals_name ON chemicals(name);

-- ============================================
-- CHEMICAL 3D ASSETS (Mô hình 3D của chất)
-- Hỗ trợ nhiều loại view: molecular model, crystal structure, etc.
-- ============================================
CREATE TABLE IF NOT EXISTS chemical_3d_assets (
    id              BIGSERIAL PRIMARY KEY,
    chemical_id     BIGINT NOT NULL,
    asset_type      VARCHAR(50) NOT NULL,      -- 'molecular_model', 'crystal_structure', 'ball_stick'
    file_url        VARCHAR(500) NOT NULL,      -- URL to .glb/.gltf file
    thumbnail_url   VARCHAR(500),
    file_format     VARCHAR(20) NOT NULL DEFAULT 'glb',
    file_size_bytes BIGINT,
    description     VARCHAR(500),
    is_default      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_c3d_chemical FOREIGN KEY (chemical_id) REFERENCES chemicals(id) ON DELETE CASCADE
);

CREATE INDEX idx_c3d_chemical ON chemical_3d_assets(chemical_id);
