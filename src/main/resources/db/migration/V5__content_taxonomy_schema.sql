-- ============================================
-- V5: Content Taxonomy Schema
-- (Lớp -> Môn -> Chương -> Bài)
-- ============================================

-- ============================================
-- GRADE LEVELS (Lớp 6, 7, 8)
-- ============================================
CREATE TABLE IF NOT EXISTS grade_levels (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,   -- 'Lớp 6', 'Lớp 7', 'Lớp 8'
    grade_num   INT NOT NULL UNIQUE,           -- 6, 7, 8
    sort_order  INT NOT NULL DEFAULT 0,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================
-- SUBJECTS (Hoá, Lý, Sinh per grade)
-- ============================================
CREATE TYPE subject_type AS ENUM ('CHEMISTRY', 'PHYSICS', 'BIOLOGY');

CREATE TABLE IF NOT EXISTS subjects (
    id              BIGSERIAL PRIMARY KEY,
    grade_level_id  BIGINT NOT NULL,
    type            subject_type NOT NULL,
    name            VARCHAR(100) NOT NULL,     -- 'Hoá học 8', 'Vật lý 8', 'Sinh học 8'
    description     TEXT,
    icon_url        VARCHAR(500),
    sort_order      INT NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_subject_grade FOREIGN KEY (grade_level_id) REFERENCES grade_levels(id),
    CONSTRAINT uq_subject_grade_type UNIQUE (grade_level_id, type)
);

-- ============================================
-- CHAPTERS (Chương)
-- ============================================
CREATE TABLE IF NOT EXISTS chapters (
    id          BIGSERIAL PRIMARY KEY,
    subject_id  BIGINT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    chapter_num INT NOT NULL,
    description TEXT,
    sort_order  INT NOT NULL DEFAULT 0,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_chapter_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

-- ============================================
-- LESSONS (Bài học)
-- ============================================
CREATE TABLE IF NOT EXISTS lessons (
    id              BIGSERIAL PRIMARY KEY,
    chapter_id      BIGINT NOT NULL,
    name            VARCHAR(255) NOT NULL,     -- 'Bài 2 - Phản ứng hoá học'
    lesson_num      INT NOT NULL,
    textbook_page   INT,
    description     TEXT,
    sort_order      INT NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_lesson_chapter FOREIGN KEY (chapter_id) REFERENCES chapters(id)
);

-- ============================================
-- SEED DATA: Grade Levels
-- ============================================
INSERT INTO grade_levels (name, grade_num, sort_order) VALUES
    ('Lớp 6', 6, 1),
    ('Lớp 7', 7, 2),
    ('Lớp 8', 8, 3);

-- ============================================
-- SEED DATA: Subjects for each grade
-- ============================================
INSERT INTO subjects (grade_level_id, type, name, sort_order) VALUES
    -- Lớp 6
    ((SELECT id FROM grade_levels WHERE grade_num = 6), 'CHEMISTRY', 'Hoá học 6', 1),
    ((SELECT id FROM grade_levels WHERE grade_num = 6), 'PHYSICS', 'Vật lý 6', 2),
    ((SELECT id FROM grade_levels WHERE grade_num = 6), 'BIOLOGY', 'Sinh học 6', 3),
    -- Lớp 7
    ((SELECT id FROM grade_levels WHERE grade_num = 7), 'CHEMISTRY', 'Hoá học 7', 1),
    ((SELECT id FROM grade_levels WHERE grade_num = 7), 'PHYSICS', 'Vật lý 7', 2),
    ((SELECT id FROM grade_levels WHERE grade_num = 7), 'BIOLOGY', 'Sinh học 7', 3),
    -- Lớp 8
    ((SELECT id FROM grade_levels WHERE grade_num = 8), 'CHEMISTRY', 'Hoá học 8', 1),
    ((SELECT id FROM grade_levels WHERE grade_num = 8), 'PHYSICS', 'Vật lý 8', 2),
    ((SELECT id FROM grade_levels WHERE grade_num = 8), 'BIOLOGY', 'Sinh học 8', 3);
