-- ============================================
-- V7: Experiments Schema
-- (Thí nghiệm 3D - Hoá học / Vật lý)
-- Cấu trúc dựa trên khtn8_chemistry_experiments.json
-- ============================================

-- ============================================
-- EXPERIMENTS (Thí nghiệm chính)
-- ============================================
CREATE TABLE IF NOT EXISTS experiments (
    id                      BIGSERIAL PRIMARY KEY,
    lesson_id               BIGINT NOT NULL,
    experiment_code         VARCHAR(100) NOT NULL UNIQUE,   -- 'exp_02_01_phase_change'
    title                   VARCHAR(500) NOT NULL,
    objective               TEXT,
    expected_observations   TEXT,
    explanation             TEXT,
    coordinate_convention   TEXT,                           -- quy ước toạ độ 3D
    safety_notes            JSONB,                         -- mảng string: ["Cẩn thận...", "Đeo kính..."]
    textbook_page           INT,
    sort_order              INT NOT NULL DEFAULT 0,
    is_active               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_exp_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

CREATE INDEX idx_exp_code ON experiments(experiment_code);
CREATE INDEX idx_exp_lesson ON experiments(lesson_id);

-- ============================================
-- EXPERIMENT CHEMICALS (Hoá chất dùng trong TN)
-- Mapping: materials.chemicals trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_chemicals (
    id              BIGSERIAL PRIMARY KEY,
    experiment_id   BIGINT NOT NULL,
    chemical_id     BIGINT,                    -- FK to chemicals library (nullable nếu chất chưa có)
    name            VARCHAR(255) NOT NULL,     -- 'Dung dịch HCl 1M', 'Nước đá viên'
    formula         VARCHAR(100),              -- 'HCl', 'H2O (rắn)'
    state           VARCHAR(50),               -- 'liquid', 'solid', 'gas', 'solid_powder', 'solid tablet'
    color           VARCHAR(50),               -- '#3fa9d6', 'transparent', 'white'
    amount          VARCHAR(100),              -- '3 mL', 'vài viên', '~1 g'
    hazard          VARCHAR(255),              -- mô tả ngắn rủi ro
    sort_order      INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_ec_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE,
    CONSTRAINT fk_ec_chem FOREIGN KEY (chemical_id) REFERENCES chemicals(id)
);

CREATE INDEX idx_ec_exp ON experiment_chemicals(experiment_id);

-- ============================================
-- EXPERIMENT EQUIPMENT (Dụng cụ TN)
-- Mapping: materials.equipment trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_equipment (
    id              BIGSERIAL PRIMARY KEY,
    experiment_id   BIGINT NOT NULL,
    name            VARCHAR(255) NOT NULL,     -- 'Ống nghiệm', 'Đèn cồn', 'Cân điện tử'
    equipment_key   VARCHAR(100),              -- key to shared_3d_objects: 'test_tube', 'bunsen_burner'
    quantity        INT NOT NULL DEFAULT 1,
    sort_order      INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_ee_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE
);

CREATE INDEX idx_ee_exp ON experiment_equipment(experiment_id);

-- ============================================
-- EXPERIMENT STEPS (Các bước thực hiện)
-- Mapping: procedure_steps trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_steps (
    id              BIGSERIAL PRIMARY KEY,
    experiment_id   BIGINT NOT NULL,
    step_number     INT NOT NULL,
    instruction     TEXT NOT NULL,             -- 'Đặt cốc thuỷ tinh chứa nước đá viên lên kiềng sắt.'
    visual_change   TEXT,                      -- mô tả hiệu ứng 3D cho step này
    sort_order      INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_es_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE
);

CREATE INDEX idx_es_exp ON experiment_steps(experiment_id);

-- ============================================
-- EXPERIMENT EQUATIONS (Phương trình hoá học)
-- Mapping: chemical_equations trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_equations (
    id              BIGSERIAL PRIMARY KEY,
    experiment_id   BIGINT NOT NULL,
    equation        VARCHAR(500) NOT NULL,     -- 'Fe + 2HCl -> FeCl2 + H2'
    description     TEXT,                      -- giải thích thêm nếu cần
    sort_order      INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_eq_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE
);

CREATE INDEX idx_eq_exp ON experiment_equations(experiment_id);

-- ============================================
-- EXPERIMENT SCENE OBJECTS (Vật thể 3D trong scene)
-- Mapping: 3d_modeling_guide.scene_objects trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_scene_objects (
    id                  BIGSERIAL PRIMARY KEY,
    experiment_id       BIGINT NOT NULL,
    object_key          VARCHAR(100) NOT NULL,     -- 'beaker_1', 'test_tube_A', 'bunsen_burner_1'
    object_type         VARCHAR(100) NOT NULL,     -- 'beaker', 'test_tube', 'bunsen_burner', 'thermometer'
    initial_position_x  DECIMAL(8,2) NOT NULL DEFAULT 0,
    initial_position_y  DECIMAL(8,2) NOT NULL DEFAULT 0,
    initial_position_z  DECIMAL(8,2) NOT NULL DEFAULT 0,
    contents            TEXT,                      -- 'Fe+S powder mix', 'HCl 0,1M + Fe nail'
    label               VARCHAR(255),              -- nhãn hiển thị trên 3D
    initial_state       VARCHAR(100),              -- 'off', 'on', etc.
    reading_range       VARCHAR(100),              -- '0->100 (C)' for thermometer
    extra_props         JSONB,                     -- flexible additional properties
    sort_order          INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_so_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE
);

CREATE INDEX idx_so_exp ON experiment_scene_objects(experiment_id);

-- ============================================
-- EXPERIMENT ANIMATIONS (Timeline animation)
-- Mapping: 3d_modeling_guide.animation_timeline trong JSON
-- ============================================
CREATE TABLE IF NOT EXISTS experiment_animations (
    id              BIGSERIAL PRIMARY KEY,
    experiment_id   BIGINT NOT NULL,
    time_index      DECIMAL(6,2) NOT NULL,     -- t: 0, 1, 2, ... (thời điểm trên timeline)
    action          VARCHAR(100) NOT NULL,      -- 'ignite_burner', 'melt_ice', 'add_HCl_to_A'
    object_key      VARCHAR(100),              -- references scene_object.object_key (nullable for global actions)
    effect          TEXT NOT NULL,              -- 'flame_ignite', 'bubble_emission rate=high', 'color_change tím->đỏ'
    sort_order      INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_anim_exp FOREIGN KEY (experiment_id) REFERENCES experiments(id) ON DELETE CASCADE
);

CREATE INDEX idx_anim_exp ON experiment_animations(experiment_id);
