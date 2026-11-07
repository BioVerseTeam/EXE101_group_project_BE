-- ============================================
-- V30: Unify plan features across all subscription packages
-- All student packages share the exact same set of premium features
-- ============================================

UPDATE plans
SET features = '["Lý thuyết 3D", "Bài tập & đề thi", "Chat AI", "Ưu tiên hỗ trợ", "Mở khoá toàn bộ Lab"]'::jsonb,
    updated_at = NOW();
