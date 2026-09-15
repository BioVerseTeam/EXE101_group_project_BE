-- ============================================
-- V11: Tách user role thành bảng riêng
-- ============================================

-- ============================================
-- ROLES TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS roles (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,   -- 'STUDENT', 'ADMIN'
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO roles (code, name, description) VALUES
    ('STUDENT', 'Student', 'Học sinh / người dùng thông thường'),
    ('ADMIN',   'Admin',   'Quản trị viên hệ thống')
ON CONFLICT (code) DO NOTHING;

-- ============================================
-- USERS: enum role -> FK role_id
-- ============================================
ALTER TABLE users ADD COLUMN IF NOT EXISTS role_id BIGINT;

UPDATE users u
SET role_id = r.id
FROM roles r
WHERE r.code = u.role::text
  AND u.role_id IS NULL;

UPDATE users
SET role_id = (SELECT id FROM roles WHERE code = 'STUDENT')
WHERE role_id IS NULL;

ALTER TABLE users
    ALTER COLUMN role_id SET NOT NULL;

DO $$
DECLARE
    student_id BIGINT;
BEGIN
    SELECT id INTO student_id FROM roles WHERE code = 'STUDENT';
    EXECUTE format('ALTER TABLE users ALTER COLUMN role_id SET DEFAULT %s', student_id);
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_users_role'
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT fk_users_role
            FOREIGN KEY (role_id) REFERENCES roles(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_users_role_id ON users(role_id);

DROP INDEX IF EXISTS idx_users_role;
ALTER TABLE users DROP COLUMN IF EXISTS role;
DROP TYPE IF EXISTS user_role;
