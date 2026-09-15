-- ============================================
-- V4: Payment & Revenue Schema (PayOS)
-- ============================================

-- ENUM TYPES
CREATE TYPE payment_status AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED', 'CANCELLED');
CREATE TYPE payment_method AS ENUM ('PAYOS', 'BANK_TRANSFER', 'FREE');

-- ============================================
-- PAYMENTS
-- ============================================
CREATE TABLE IF NOT EXISTS payments (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    subscription_id     BIGINT,
    -- PayOS fields
    order_code          BIGINT NOT NULL UNIQUE,    -- PayOS orderCode (unique int64)
    payos_payment_link  VARCHAR(500),              -- link thanh toán PayOS
    payos_transaction_id VARCHAR(255),             -- PayOS transaction reference
    -- Money
    amount              DECIMAL(12,2) NOT NULL,    -- VND
    currency            VARCHAR(3) NOT NULL DEFAULT 'VND',
    -- Status
    status              payment_status NOT NULL DEFAULT 'PENDING',
    method              payment_method NOT NULL DEFAULT 'PAYOS',
    -- Metadata
    description         VARCHAR(500),
    paid_at             TIMESTAMP,
    failed_reason       VARCHAR(500),
    -- Webhook data từ PayOS (lưu nguyên JSON để audit)
    webhook_data        JSONB,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_payment_sub FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

CREATE INDEX idx_payment_user ON payments(user_id);
CREATE INDEX idx_payment_order_code ON payments(order_code);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_created ON payments(created_at);

-- ============================================
-- REVENUE DAILY SNAPSHOT (cho dashboard Admin)
-- Cron job aggregate mỗi ngày từ payments
-- ============================================
CREATE TABLE IF NOT EXISTS revenue_daily (
    id              BIGSERIAL PRIMARY KEY,
    report_date     DATE NOT NULL UNIQUE,
    total_revenue   DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_orders    INT NOT NULL DEFAULT 0,
    new_subscribers INT NOT NULL DEFAULT 0,
    churned         INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_revenue_date ON revenue_daily(report_date);
