-- ============================================
-- V10: Shared 3D Objects Schema
-- (Thư viện dụng cụ TN 3D dùng chung)
-- ============================================

-- ============================================
-- SHARED 3D OBJECTS
-- Mapping từ shared_object_library trong JSON
-- object_key dùng để link với experiment_equipment.equipment_key
-- và experiment_scene_objects.object_type
-- ============================================
CREATE TABLE IF NOT EXISTS shared_3d_objects (
    id              BIGSERIAL PRIMARY KEY,
    object_key      VARCHAR(100) NOT NULL UNIQUE,  -- 'test_tube', 'beaker', 'bunsen_burner'
    name            VARCHAR(255) NOT NULL,         -- 'Test Tube'
    name_vi         VARCHAR(255),                  -- 'Ống nghiệm'
    description     TEXT,                          -- 'Ống nghiệm thuỷ tinh hình trụ, đáy tròn, cao ~15 unit, đường kính ~2 unit'
    model_url       VARCHAR(500),                  -- URL .glb file
    thumbnail_url   VARCHAR(500),
    category        VARCHAR(100),                  -- 'glassware', 'heating', 'measuring', 'safety', 'tools'
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================
-- SEED DATA: Shared 3D Objects từ shared_object_library trong JSON
-- ============================================
INSERT INTO shared_3d_objects (object_key, name, name_vi, description, category) VALUES
    ('test_tube',               'Test Tube',                'Ống nghiệm',              'Ống nghiệm thuỷ tinh hình trụ, đáy tròn, cao ~15 unit, đường kính ~2 unit', 'glassware'),
    ('test_tube_rack',          'Test Tube Rack',           'Giá đỡ ống nghiệm',       'Giá đỡ ống nghiệm, giữ ống ở góc thẳng đứng', 'glassware'),
    ('beaker',                  'Beaker',                   'Cốc thuỷ tinh',           'Cốc thuỷ tinh hình trụ đáy rộng, có vạch chia mL', 'glassware'),
    ('burette_dropper',         'Dropper',                  'Ống hút nhỏ giọt',        'Ống hút nhỏ giọt / ống nhỏ giọt cao su', 'tools'),
    ('bunsen_burner',           'Bunsen Burner',            'Đèn cồn / đèn Bunsen',   'Đèn cồn / đèn Bunsen, có ngọn lửa vàng-cam khi bật', 'heating'),
    ('iron_stand_clamp',        'Iron Stand & Clamp',       'Giá sắt + kẹp',          'Giá sắt + kẹp giữ ống nghiệm/cốc khi đun', 'tools'),
    ('electronic_balance',      'Electronic Balance',       'Cân điện tử',             'Cân điện tử, có màn hình số hiển thị khối lượng (gam)', 'measuring'),
    ('ph_paper_strip',          'pH Paper Strip',           'Giấy pH',                 'Giấy pH dài ~1cm, đổi màu theo thang màu chuẩn 1-14', 'measuring'),
    ('litmus_paper',            'Litmus Paper',             'Giấy quỳ tím',            'Giấy quỳ tím, đổi màu: xanh (base), đỏ (acid), tím (trung tính)', 'measuring'),
    ('phenolphthalein_indicator','Phenolphthalein',          'Phenolphthalein',         'Chất chỉ thị không màu, chuyển hồng khi gặp base', 'measuring'),
    ('l_shaped_glass_tube',     'L-Shaped Glass Tube',      'Ống thuỷ tinh chữ L',    'Ống thuỷ tinh hình chữ L để dẫn khí từ ống nghiệm này sang ống nghiệm khác', 'glassware'),
    ('rubber_stopper',          'Rubber Stopper',           'Nút cao su',              'Nút cao su có lỗ xuyên để cắm ống dẫn khí', 'tools'),
    ('magnet',                  'Magnet',                   'Nam châm',                'Nam châm thử, dùng để kiểm tra tính chất từ', 'tools'),
    ('spatula_spoon',           'Spatula / Spoon',          'Thìa lấy hoá chất',      'Thìa lấy hoá chất rắn (kim loại hoặc nhựa/thuỷ tinh)', 'tools');
