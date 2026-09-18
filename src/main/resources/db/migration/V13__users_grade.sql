-- ============================================
-- V13: Lớp đang học trên hồ sơ user (6, 7, 8, 9)
-- ============================================

ALTER TABLE users ADD COLUMN IF NOT EXISTS grade INT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'chk_users_grade'
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT chk_users_grade
            CHECK (grade IS NULL OR grade BETWEEN 6 AND 9);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_users_grade ON users(grade);
