-- ============================================
-- V9: Exam Enhancement Schema
-- (Bổ sung cho schema exam hiện tại V1)
-- ============================================

-- ============================================
-- Link exam to subject & grade (thêm cột cho bảng exam đã có)
-- ============================================
ALTER TABLE exam ADD COLUMN IF NOT EXISTS subject_id BIGINT;
ALTER TABLE exam ADD COLUMN IF NOT EXISTS grade_level_id BIGINT;
ALTER TABLE exam ADD COLUMN IF NOT EXISTS duration_minutes INT DEFAULT 45;
ALTER TABLE exam ADD COLUMN IF NOT EXISTS total_score DOUBLE PRECISION DEFAULT 10.0;
ALTER TABLE exam ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- Add foreign keys (dùng DO block để check trước khi add)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_subject') THEN
        ALTER TABLE exam ADD CONSTRAINT fk_exam_subject FOREIGN KEY (subject_id) REFERENCES subjects(id);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_grade') THEN
        ALTER TABLE exam ADD CONSTRAINT fk_exam_grade FOREIGN KEY (grade_level_id) REFERENCES grade_levels(id);
    END IF;
END$$;

CREATE INDEX IF NOT EXISTS idx_exam_subject ON exam(subject_id);
CREATE INDEX IF NOT EXISTS idx_exam_grade ON exam(grade_level_id);

-- ============================================
-- EXAM ATTEMPTS (Lưu lịch sử làm bài của student)
-- ============================================
CREATE TABLE IF NOT EXISTS exam_attempts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    exam_id         BIGINT NOT NULL,
    score           DOUBLE PRECISION,
    total_questions INT,
    correct_count   INT,
    started_at      TIMESTAMP NOT NULL,
    submitted_at    TIMESTAMP,
    time_spent_sec  INT,                       -- tổng thời gian làm (giây)
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_attempt_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_attempt_exam FOREIGN KEY (exam_id) REFERENCES exam(id)
);

CREATE INDEX idx_attempt_user ON exam_attempts(user_id);
CREATE INDEX idx_attempt_exam ON exam_attempts(exam_id);

-- ============================================
-- ATTEMPT ANSWERS (Chi tiết từng câu trả lời)
-- ============================================
CREATE TABLE IF NOT EXISTS attempt_answers (
    id                  BIGSERIAL PRIMARY KEY,
    attempt_id          BIGINT NOT NULL,
    question_id         BIGINT NOT NULL,
    selected_answer_id  BIGINT,                 -- nullable: user chưa trả lời câu này
    is_correct          BOOLEAN,
    time_spent_sec      INT,                    -- thời gian cho câu này (giây)
    CONSTRAINT fk_aa_attempt FOREIGN KEY (attempt_id) REFERENCES exam_attempts(id) ON DELETE CASCADE,
    CONSTRAINT fk_aa_question FOREIGN KEY (question_id) REFERENCES question(id),
    CONSTRAINT fk_aa_answer FOREIGN KEY (selected_answer_id) REFERENCES answer(id)
);

CREATE INDEX idx_aa_attempt ON attempt_answers(attempt_id);
