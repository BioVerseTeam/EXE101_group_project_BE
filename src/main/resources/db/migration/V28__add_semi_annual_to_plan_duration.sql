-- ============================================
-- V28: Add SEMI_ANNUAL value to plan_duration enum
-- Must execute outside transaction block in PostgreSQL
-- ============================================

-- flyway:executeInTransaction=false
ALTER TYPE plan_duration ADD VALUE IF NOT EXISTS 'SEMI_ANNUAL';
