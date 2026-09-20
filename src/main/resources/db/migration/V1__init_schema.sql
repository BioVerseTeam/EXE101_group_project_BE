-- Create exam table
CREATE TABLE exam (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255),
    type VARCHAR(255),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    name VARCHAR(255),
    subject_name VARCHAR(255),
    description VARCHAR(255)
);

-- Create question table
CREATE TABLE question (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255),
    content TEXT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    explanation VARCHAR(255),
    description VARCHAR(255)
);

-- Create exam_question link table
CREATE TABLE exam_question (
    id BIGSERIAL PRIMARY KEY,
    point DOUBLE PRECISION NOT NULL,
    question_order INT NOT NULL,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    exam_id BIGINT,
    question_id BIGINT,
    CONSTRAINT fk_exam_question_exam FOREIGN KEY (exam_id) REFERENCES exam(id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_question_question FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- Create question_image table
CREATE TABLE question_image (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    display_order INT NOT NULL,
    created_date TIMESTAMP,
    url VARCHAR(255),
    question_id BIGINT,
    CONSTRAINT fk_question_image_question FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- Create answer table
CREATE TABLE answer (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255),
    content TEXT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    explanation VARCHAR(255),
    description VARCHAR(255),
    is_correct BOOLEAN NOT NULL,
    question_id BIGINT,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- Create answer_image table
CREATE TABLE answer_image (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    display_order INT NOT NULL,
    created_date TIMESTAMP,
    url VARCHAR(255),
    answer_id BIGINT,
    CONSTRAINT fk_answer_image_answer FOREIGN KEY (answer_id) REFERENCES answer(id) ON DELETE CASCADE
);
