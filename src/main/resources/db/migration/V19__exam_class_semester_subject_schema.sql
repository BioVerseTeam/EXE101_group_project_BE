-- ============================================
-- V16: Exam Taxonomy Schema (Grade -> Semester -> Subject -> Exam)
-- Quản lý đề thi theo Khối lớp (grades), Học kỳ (semesters) và Môn học (subjects)
-- ============================================

-- Dọn dẹp/đổi tên bảng cũ nếu đã tồn tại từ lần chạy trước
DO $$
BEGIN
    -- Nếu bảng exam_classes đã tồn tại, đổi tên sang grades
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'exam_classes') 
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'grades') THEN
        ALTER TABLE exam_classes RENAME TO grades;
    END IF;

    -- Nếu bảng exam_semesters đã tồn tại, đổi tên sang semesters
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'exam_semesters') 
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'semesters') THEN
        ALTER TABLE exam_semesters RENAME TO semesters;
    END IF;

    -- Nếu bảng subjects cũ từ V5 tồn tại mà không có semester_id, xóa bảng cũ để tạo lại theo schema mới
    IF EXISTS (
        SELECT 1 FROM information_schema.tables WHERE table_name = 'subjects'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns WHERE table_name = 'subjects' AND column_name = 'semester_id'
    ) THEN
        DROP TABLE IF EXISTS lessons CASCADE;
        DROP TABLE IF EXISTS chapters CASCADE;
        DROP TABLE IF EXISTS subjects CASCADE;
    END IF;

    -- Nếu bảng exam_subjects đã tồn tại, đổi tên sang subjects
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'exam_subjects') 
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'subjects') THEN
        ALTER TABLE exam_subjects RENAME TO subjects;
    END IF;
END $$;

-- 1. BẢNG KHỐI LỚP (grades)
CREATE TABLE IF NOT EXISTS grades (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    grade           INT NOT NULL UNIQUE,
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 2. BẢNG HỌC KỲ (semesters)
CREATE TABLE IF NOT EXISTS semesters (
    id              BIGSERIAL PRIMARY KEY,
    class_id        BIGINT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    semester_order  INT NOT NULL DEFAULT 1,
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_semester_grade FOREIGN KEY (class_id) REFERENCES grades(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_semesters_class ON semesters(class_id);

-- 3. BẢNG MÔN HỌC THEO HỌC KỲ (subjects)
CREATE TABLE IF NOT EXISTS subjects (
    id              BIGSERIAL PRIMARY KEY,
    semester_id     BIGINT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    code            VARCHAR(50),
    description     TEXT,
    created_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_subject_semester FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_subjects_semester ON subjects(semester_id);

-- 4. LIÊN KẾT BẢNG EXAM VỚI SUBJECTS
DO $$
BEGIN
    -- Đảm bảo cột subject_id tồn tại trên bảng exam
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'exam' AND column_name = 'subject_id'
    ) THEN
        ALTER TABLE exam ADD COLUMN subject_id BIGINT;
    END IF;

    -- Xóa constraint cũ nếu trỏ nhầm
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_subject') THEN
        ALTER TABLE exam DROP CONSTRAINT fk_exam_subject;
    END IF;
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_exam_subject') THEN
        ALTER TABLE exam DROP CONSTRAINT fk_exam_exam_subject;
    END IF;

    -- Tạo foreign key liên kết với subjects
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_subject') THEN
        ALTER TABLE exam ADD CONSTRAINT fk_exam_subject 
            FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL;
    END IF;

    -- Đảm bảo các cột mở rộng cho Exam entity tồn tại
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'exam' AND column_name = 'duration_minutes'
    ) THEN
        ALTER TABLE exam ADD COLUMN duration_minutes INT DEFAULT 45;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'exam' AND column_name = 'total_score'
    ) THEN
        ALTER TABLE exam ADD COLUMN total_score DOUBLE PRECISION DEFAULT 10.0;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'exam' AND column_name = 'is_active'
    ) THEN
        ALTER TABLE exam ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_exam_subject_id ON exam(subject_id);
