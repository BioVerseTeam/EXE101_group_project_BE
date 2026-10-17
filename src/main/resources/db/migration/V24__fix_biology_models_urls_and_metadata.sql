-- ============================================
-- V24: Fix biology model URLs and metadata
-- Correct nervous system, digestive system, and muscular system 3D models
-- ============================================

UPDATE bio_models 
SET model_url = '/api/models/nervous_system.glb',
    badge_text = '139 Bộ phận 3D',
    description = 'Giải phẫu 3D chuyên sâu Hệ Thần Kinh: Bán cầu đại não, Thân não, Tiểu não, Tủy sống và các Dây thần kinh sọ.',
    target_mode = 'skull'
WHERE slug = 'he-than-kinh';

UPDATE bio_models 
SET model_url = '/api/models/digestive_system.glb',
    target_mode = 'organs'
WHERE slug = 'he-tieu-hoa';

UPDATE bio_models 
SET model_url = '/api/models/muscular_system.glb',
    target_mode = 'paramecium'
WHERE slug = 'he-co-bap';
