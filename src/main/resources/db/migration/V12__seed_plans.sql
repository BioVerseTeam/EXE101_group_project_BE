-- ============================================
-- V12: Seed subscription plans
-- ============================================

INSERT INTO plans (name, slug, description, duration, duration_days, price, original_price, features, max_devices, status, sort_order)
VALUES
    (
        'Gói Tháng',
        'monthly',
        'Truy cập đầy đủ nội dung trong 30 ngày',
        'MONTHLY'::plan_duration,
        30,
        49000,
        79000,
        '["Lý thuyết 3D","Bài tập & đề thi","Chat AI"]'::jsonb,
        1,
        'ACTIVE'::plan_status,
        1
    ),
    (
        'Gói Quý',
        'quarterly',
        'Tiết kiệm hơn khi học theo học kỳ',
        'QUARTERLY'::plan_duration,
        90,
        129000,
        237000,
        '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Ưu tiên hỗ trợ"]'::jsonb,
        2,
        'ACTIVE'::plan_status,
        2
    ),
    (
        'Gói Năm',
        'yearly',
        'Học cả năm với mức giá tốt nhất',
        'YEARLY'::plan_duration,
        365,
        399000,
        948000,
        '["Lý thuyết 3D","Bài tập & đề thi","Chat AI","Ưu tiên hỗ trợ","Nhiều thiết bị"]'::jsonb,
        3,
        'ACTIVE'::plan_status,
        3
    )
ON CONFLICT (slug) DO NOTHING;
