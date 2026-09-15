-- ============================================
-- V3: Subscription Plans Schema
-- ============================================

-- ENUM TYPES
CREATE TYPE plan_duration AS ENUM ('MONTHLY', 'QUARTERLY', 'YEARLY');
CREATE TYPE plan_status AS ENUM ('ACTIVE', 'INACTIVE', 'ARCHIVED');
CREATE TYPE subscription_status AS ENUM ('ACTIVE', 'EXPIRED', 'CANCELLED', 'PENDING');

-- ============================================
-- PLANS (Các gói cước - Admin quản lý)
-- ============================================
CREATE TABLE IF NOT EXISTS plans (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    duration        plan_duration NOT NULL,
    duration_days   INT NOT NULL,              -- 30, 90, 365
    price           DECIMAL(12,2) NOT NULL,    -- VND
    original_price  DECIMAL(12,2),             -- giá gốc (dùng để hiện giảm giá)
    features        JSONB,                     -- danh sách features hiển thị UI
    max_devices     INT NOT NULL DEFAULT 1,    -- số thiết bị đồng thời
    status          plan_status NOT NULL DEFAULT 'ACTIVE',
    sort_order      INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================
-- SUBSCRIPTIONS (User mua gói)
-- ============================================
CREATE TABLE IF NOT EXISTS subscriptions (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    plan_id         BIGINT NOT NULL,
    status          subscription_status NOT NULL DEFAULT 'PENDING',
    start_date      TIMESTAMP,
    end_date        TIMESTAMP,
    auto_renew      BOOLEAN NOT NULL DEFAULT FALSE,
    cancelled_at    TIMESTAMP,
    cancel_reason   VARCHAR(500),
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_sub_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_sub_plan FOREIGN KEY (plan_id) REFERENCES plans(id)
);

CREATE INDEX idx_sub_user ON subscriptions(user_id);
CREATE INDEX idx_sub_status ON subscriptions(status);
CREATE INDEX idx_sub_end_date ON subscriptions(end_date);
