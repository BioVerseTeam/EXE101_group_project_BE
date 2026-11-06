-- ============================================
-- V29: Update subscription plans to Student packages
-- 1. Student 1 month: 69,000 VND (30 days)
-- 2. Student 3 months: 199,000 VND (90 days)
-- 3. Student 6 months: 389,000 VND (180 days)
-- 4. Student 1 year: 749,000 VND (365 days)
-- ============================================

-- 1. Student 1 month
INSERT INTO plans (name, slug, description, duration, duration_days, price, original_price, features, max_devices, status, sort_order)
VALUES (
    'Student 1 month',
    'monthly',
    'Truy cập đầy đủ nội dung trong 30 ngày',
    'MONTHLY'::plan_duration,
    30,
    69000,
    89000,
    '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Học trên mọi thiết bị"]'::jsonb,
    1,
    'ACTIVE'::plan_status,
    1
)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    duration = EXCLUDED.duration,
    duration_days = EXCLUDED.duration_days,
    price = EXCLUDED.price,
    original_price = EXCLUDED.original_price,
    status = EXCLUDED.status,
    sort_order = EXCLUDED.sort_order,
    updated_at = NOW();

-- 2. Student 3 months
INSERT INTO plans (name, slug, description, duration, duration_days, price, original_price, features, max_devices, status, sort_order)
VALUES (
    'Student 3 months',
    'quarterly',
    'Tiết kiệm hơn khi học theo học kỳ',
    'QUARTERLY'::plan_duration,
    90,
    199000,
    267000,
    '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Ưu tiên hỗ trợ","Mở khoá toàn bộ Lab"]'::jsonb,
    2,
    'ACTIVE'::plan_status,
    2
)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    duration = EXCLUDED.duration,
    duration_days = EXCLUDED.duration_days,
    price = EXCLUDED.price,
    original_price = EXCLUDED.original_price,
    status = EXCLUDED.status,
    sort_order = EXCLUDED.sort_order,
    updated_at = NOW();

-- 3. Student 6 months
INSERT INTO plans (name, slug, description, duration, duration_days, price, original_price, features, max_devices, status, sort_order)
VALUES (
    'Student 6 months',
    'semi-annual',
    'Gói học tập bán niên 180 ngày toàn diện',
    'SEMI_ANNUAL'::plan_duration,
    180,
    389000,
    534000,
    '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Ưu tiên hỗ trợ","Mở khoá toàn bộ Lab"]'::jsonb,
    2,
    'ACTIVE'::plan_status,
    3
)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    duration = EXCLUDED.duration,
    duration_days = EXCLUDED.duration_days,
    price = EXCLUDED.price,
    original_price = EXCLUDED.original_price,
    features = EXCLUDED.features,
    max_devices = EXCLUDED.max_devices,
    status = EXCLUDED.status,
    sort_order = EXCLUDED.sort_order,
    updated_at = NOW();

-- 4. Student 1 year
INSERT INTO plans (name, slug, description, duration, duration_days, price, original_price, features, max_devices, status, sort_order)
VALUES (
    'Student 1 year',
    'yearly',
    'Học cả năm với mức giá tốt nhất',
    'YEARLY'::plan_duration,
    365,
    749000,
    1068000,
    '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Ưu tiên hỗ trợ","Mọi tính năng mới sớm nhất"]'::jsonb,
    3,
    'ACTIVE'::plan_status,
    4
)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    duration = EXCLUDED.duration,
    duration_days = EXCLUDED.duration_days,
    price = EXCLUDED.price,
    original_price = EXCLUDED.original_price,
    status = EXCLUDED.status,
    sort_order = EXCLUDED.sort_order,
    updated_at = NOW();
