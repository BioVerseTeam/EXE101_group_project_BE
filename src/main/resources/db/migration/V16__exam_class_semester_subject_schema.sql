-- ============================================
-- V16: Exam Taxonomy Schema (Class -> Semester -> Subject -> Exam)
-- Quản lý đề thi theo Khối lớp (6, 7, 8, 9), Học kỳ (1, 2) và Môn học
-- ============================================

-- 1. BẢNG KHỐI LỚP (ClassEntity)
CREATE TABLE IF NOT EXISTS exam_classes (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    grade           INT NOT NULL UNIQUE,
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 2. BẢNG HỌC KỲ (Semester)
CREATE TABLE IF NOT EXISTS exam_semesters (
    id              BIGSERIAL PRIMARY KEY,
    class_id        BIGINT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    semester_order  INT NOT NULL DEFAULT 1,
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_exam_semester_class FOREIGN KEY (class_id) REFERENCES exam_classes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_exam_semesters_class ON exam_semesters(class_id);

-- 3. BẢNG MÔN HỌC THEO HỌC KỲ (Subject)
CREATE TABLE IF NOT EXISTS exam_subjects (
    id              BIGSERIAL PRIMARY KEY,
    semester_id     BIGINT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    code            VARCHAR(50),
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_exam_subject_semester FOREIGN KEY (semester_id) REFERENCES exam_semesters(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_exam_subjects_semester ON exam_subjects(semester_id);

-- 4. LIÊN KẾT BẢNG EXAM VỚI EXAM_SUBJECTS
DO $$
BEGIN
    -- Đảm bảo cột subject_id tồn tại trên bảng exam
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'exam' AND column_name = 'subject_id'
    ) THEN
        ALTER TABLE exam ADD COLUMN subject_id BIGINT;
    END IF;

    -- Xóa constraint cũ nếu trỏ nhầm sang bảng subjects của taxonomy V5
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_subject') THEN
        ALTER TABLE exam DROP CONSTRAINT fk_exam_subject;
    END IF;

    -- Tạo foreign key liên kết với exam_subjects
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_exam_subject') THEN
        ALTER TABLE exam ADD CONSTRAINT fk_exam_exam_subject 
            FOREIGN KEY (subject_id) REFERENCES exam_subjects(id) ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_exam_subject_id ON exam(subject_id);
