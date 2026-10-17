-- ==============================================================================
-- V23: Seed Physics 3D Models Interactive Quizzes (Exam Module)
-- Tạo 12 đề kiểm tra (Exam), 60 câu hỏi (Question), 240 đáp án (Answer) và
-- 60 liên kết (ExamQuestion) bằng các entity chuẩn của module exam cho 12 mô hình 3D Vật lý
-- ==============================================================================

-- 1. Bổ sung cột model_id cho bảng exam để liên kết trực tiếp với bio_models
ALTER TABLE exam ADD COLUMN IF NOT EXISTS model_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_exam_model') THEN
        ALTER TABLE exam ADD CONSTRAINT fk_exam_model 
            FOREIGN KEY (model_id) REFERENCES bio_models(id) ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_exam_model_id ON exam(model_id);

-- 2. TẠO DỮ LIỆU ĐỀ THI VÀ BỘ CÂU HỎI CHO 12 MÔ HÌNH VẬT LÝ 3D
DO $$
DECLARE
    v_sub_id_6   BIGINT;
    v_sub_id_9   BIGINT;
    v_model_id   BIGINT;
    v_exam_id    BIGINT;
    v_q_id       BIGINT;
    v_point      DOUBLE PRECISION := 2.0; -- 5 câu x 2.0 = 10.0 điểm
BEGIN
    -- Tìm subject ID cho KHTN 6 và KHTN 9 (nếu có trong bảng subjects)
    SELECT id INTO v_sub_id_6 FROM subjects WHERE code = 'KHTN6_HK1' LIMIT 1;
    IF v_sub_id_6 IS NULL THEN
        SELECT id INTO v_sub_id_6 FROM subjects WHERE name ILIKE '%6%' LIMIT 1;
    END IF;

    SELECT id INTO v_sub_id_9 FROM subjects WHERE code = 'KHTN9_HK1' LIMIT 1;
    IF v_sub_id_9 IS NULL THEN
        SELECT id INTO v_sub_id_9 FROM subjects WHERE name ILIKE '%9%' LIMIT 1;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 1: HỆ THỐNG RÒNG RỌC (slug: he-thong-rong-roc) - LỚP 6
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'he-thong-rong-roc' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_he-thong-rong-roc' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_6, 'QUIZ_MODEL_he-thong-rong-roc',
            'Quiz 3D: Hệ Thống Ròng Rọc', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra kiến thức về ròng rọc cố định, ròng rọc động và định luật về công.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Ròng rọc cố định có tác dụng gì chủ yếu trong thực tế?',
                'Ròng rọc cố định chỉ có tác dụng làm thay đổi hướng của lực kéo (ví dụ kéo dây xuống để nâng vật lên cao), không cho ta lợi về lực (F = P) và cũng không cho lợi về đường đi.',
                'Cơ học 6 - Ròng rọc cố định', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Cho ta lợi 2 lần về lực nâng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Làm thay đổi hướng của lực kéo so với khi kéo trực tiếp mà không làm giảm độ lớn của lực.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Làm giảm công cơ học cần thực hiện đi một nửa.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cho ta lợi cả về lực kéo lẫn quãng đường đi.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi sử dụng một ròng rọc động để nâng một vật nặng, ta nhận được lợi ích cơ học nào?',
                'Một ròng rọc động giúp giảm độ lớn lực kéo đi một nửa (F = P / 2), nhưng phải kéo dây dài gấp đôi (s = 2h), tuân thủ định luật bảo toàn công.',
                'Cơ học 6 - Ròng rọc động', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Lợi 2 lần về lực nhưng thiệt 2 lần về đường đi.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lợi 2 lần về đường đi nhưng thiệt 2 lần về lực kéo.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lợi cả về lực và đường đi, công giảm đi 4 lần.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không được lợi về lực, chỉ đổi hướng của lực kéo.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Một hệ thống palăng gồm 2 ròng rọc động và 2 ròng rọc cố định. Bỏ qua ma sát và trọng lượng ròng rọc, lực kéo cần thiết để nâng vật có trọng lượng P = 400 N là bao nhiêu?',
                'Với hệ palăng gồm 2 ròng rọc động, có 4 đoạn dây cùng chịu tải nâng vật. Do đó lực kéo giảm đi 4 lần: F = P / 4 = 400 / 4 = 100 N.',
                'Cơ học 6 - Hệ thống Palăng', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', '400 N', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '200 N', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '100 N', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', '50 N', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Theo định luật về công đối với các máy cơ đơn giản (khi bỏ qua ma sát):',
                'Định luật về công khẳng định: Không một máy cơ đơn giản nào cho ta lợi về công. Được lợi bao nhiêu lần về lực thì thiệt bấy nhiêu lần về đường đi và ngược lại (A = F.s = không đổi).',
                'Cơ học 6 - Định luật về công', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Sử dụng máy cơ đơn giản luôn giúp giảm bớt công thực hiện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không một máy cơ đơn giản nào cho ta lợi về công; được lợi bao nhiêu lần về lực thì thiệt bấy nhiêu lần về đường đi và ngược lại.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Máy cơ đơn giản vừa cho ta lợi về lực vừa cho ta lợi về công.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Máy cơ đơn giản làm tăng công thực hiện lên gấp nhiều lần.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Trong thực tế, công toàn phần để nâng vật bằng hệ thống ròng rọc luôn lớn hơn công có ích vì lý do nào sau đây?',
                'Trong thực tế luôn tồn tại lực ma sát ở ổ trục và ta phải tốn thêm công để nâng chính trọng lượng của các ròng rọc động cùng dây kéo, khiến công toàn phần luôn lớn hơn công có ích (hiệu suất H < 100%).',
                'Cơ học 6 - Hiệu suất máy cơ', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Do dây kéo của ròng rọc quá dài.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Phải tốn một phần công để thắng ma sát ở ổ trục và nâng trọng lượng của ròng rọc động cùng dây.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Do định luật về công không chính xác ngoài đời thực.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Do vật bị tăng khối lượng khi được kéo lên cao.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 2: LÒ XO CUỘN (slug: lo-xo-cuon) - LỚP 6
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'lo-xo-cuon' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_lo-xo-cuon' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_6, 'QUIZ_MODEL_lo-xo-cuon',
            'Quiz 3D: Lò Xo Cuộn', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra độ dãn của lò xo, lực đàn hồi và định luật Hooke.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Lực đàn hồi của lò xo xuất hiện khi nào?',
                'Lực đàn hồi xuất hiện khi lò xo bị biến dạng đàn hồi (bị nén hoặc kéo dãn) và có xu hướng chống lại nguyên nhân làm biến dạng để đưa lò xo trở lại hình dạng ban đầu.',
                'Cơ học 6 - Lực đàn hồi', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Chỉ khi lò xo đứng yên trên mặt bàn nằm ngang.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Khi lò xo bị biến dạng và có xu hướng chống lại nguyên nhân làm nó biến dạng.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chỉ khi nung nóng lò xo đến nhiệt độ rất cao.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Khi treo lò xo trong môi trường chân không.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Trong giới hạn đàn hồi, độ dãn của lò xo treo thẳng đứng có mối liên hệ như thế nào với khối lượng quả nặng móc vào nó?',
                'Trong giới hạn đàn hồi, độ dãn của lò xo tỉ lệ thuận với trọng lượng (hay khối lượng) của quả nặng tác dụng lên lò xo (Δl ~ P = m.g).',
                'Cơ học 6 - Định luật Hooke', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Tỉ lệ nghịch với khối lượng quả nặng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tỉ lệ thuận với khối lượng quả nặng móc vào lò xo.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không phụ thuộc vào khối lượng của quả nặng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tăng theo hàm bậc hai của khối lượng quả nặng.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Một lò xo có chiều dài tự nhiên là 10 cm. Khi treo quả cân 100 g thì chiều dài là 12 cm. Nếu treo quả cân 200 g thì chiều dài của lò xo là bao nhiêu (trong giới hạn đàn hồi)?',
                'Treo 100 g thì độ dãn Δl1 = 12 - 10 = 2 cm. Khi treo 200 g (gấp đôi tải trọng), độ dãn gấp đôi: Δl2 = 2 * 2 = 4 cm. Chiều dài mới l = 10 + 4 = 14 cm.',
                'Cơ học 6 - Tính toán biến dạng', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', '13 cm', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '14 cm', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', '15 cm', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '16 cm', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Dụng cụ nào dưới đây hoạt động dựa trên ứng dụng tính biến dạng đàn hồi của lò xo?',
                'Lực kế lò xo sử dụng mối liên hệ tỉ lệ thuận giữa lực tác dụng và độ dãn của lò xo để đo độ lớn của lực.',
                'Cơ học 6 - Dụng cụ đo lực', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Lực kế lò xo.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Thước dây cuộn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cân đòn đối trọng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nhiệt kế thủy ngân.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Điều gì sẽ xảy ra nếu ta kéo dãn lò xo vượt quá giới hạn đàn hồi của nó?',
                'Khi vượt quá giới hạn đàn hồi, cấu trúc tinh thể kim loại bị trượt vĩnh viễn (biến dạng dẻo), lò xo không thể tự co về chiều dài ban đầu và bị hỏng chức năng đàn hồi.',
                'Cơ học 6 - Giới hạn đàn hồi', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Lò xo tự động co ngắn hơn chiều dài ban đầu.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lò xo bị biến dạng vĩnh viễn và không thể tự trở về hình dạng ban đầu nữa.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lực đàn hồi của lò xo tăng lên vô hạn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Khối lượng của lò xo bị tiêu hao giảm đi đáng kể.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 3: MẠCH ĐIỆN CƠ BẢN (slug: mach-dien-co-ban) - LỚP 6
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'mach-dien-co-ban' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_mach-dien-co-ban' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_6, 'QUIZ_MODEL_mach-dien-co-ban',
            'Quiz 3D: Mạch Điện Cơ Bản', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra thành phần mạch điện, chiều dòng điện và mạch kín/mạch hở.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Một mạch điện đơn giản thắp sáng bóng đèn tối thiểu cần có các bộ phận nào?',
                'Mạch điện thắp sáng bóng đèn cơ bản cần nguồn điện (pin), dây dẫn và thiết bị tiêu thụ điện (bóng đèn) nối liền thành một mạch kín.',
                'Điện học 6 - Thành phần mạch điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Nguồn điện (pin), dây dẫn và bóng đèn tạo thành mạch kín.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chỉ cần bóng đèn và công tắc mở.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nguồn điện và dây dẫn hở hai đầu.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai bóng đèn nối trực tiếp với nhau không cần nguồn.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Quy ước chiều dòng điện trong mạch điện ngoài là chiều nào?',
                'Theo quy ước quốc tế trong Vật lý, chiều dòng điện là chiều đi từ cực dương qua dây dẫn và thiết bị điện tới cực âm của nguồn điện.',
                'Điện học 6 - Chiều dòng điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Chiều từ cực âm qua dây dẫn tới cực dương của nguồn điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chiều từ cực dương qua dây dẫn và thiết bị điện tới cực âm của nguồn điện.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cùng chiều với chiều dịch chuyển của các electron tự do trong kim loại.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không có quy ước cố định, tùy thuộc vào loại bóng đèn.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vật liệu nào sau đây dẫn điện tốt nhất và thường được dùng làm lõi dây dẫn điện trong gia đình?',
                'Kim loại đồng (Cu) có mật độ electron tự do cao, điện trở suất nhỏ, dẫn điện rất tốt và mềm dẻo, giá thành phù hợp nên được dùng phổ biến làm lõi dây điện.',
                'Điện học 6 - Chất dẫn điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Cao su tổng hợp.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Kim loại đồng (Cu).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Gỗ khô.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Thủy tinh.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi công tắc (khóa K) trong mạch điện đang mở thì điều gì xảy ra?',
                'Khi công tắc mở, đường dẫn điện bị gián đoạn (mạch hở), không có dòng điện chạy qua thiết bị nên bóng đèn không sáng.',
                'Điện học 6 - Mạch hở mạch kín', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Dòng điện tăng vọt làm cháy bóng đèn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Mạch điện bị hở, không có dòng điện chạy qua và đèn không sáng.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đèn vẫn sáng nhờ điện tích dự trữ sẵn trong dây dẫn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cực dương của nguồn điện tự động chuyển sang cực âm.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi hai quả pin 1.5 V được mắc nối tiếp nhau cùng chiều trong mạch, hiệu điện thế tổng cộng cấp cho mạch là bao nhiêu?',
                'Khi ghép nối tiếp hai nguồn điện cùng chiều, hiệu điện thế của bộ nguồn bằng tổng hiệu điện thế của từng nguồn: U = U1 + U2 = 1.5 + 1.5 = 3.0 V.',
                'Điện học 6 - Ghép nguồn điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', '0 V', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '1.5 V', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '3.0 V', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', '4.5 V', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 4: VÔN KẾ (slug: von-ke) - LỚP 6
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'von-ke' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_von-ke' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_6, 'QUIZ_MODEL_von-ke',
            'Quiz 3D: Vôn Kế', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra cách sử dụng vôn kế đo hiệu điện thế và cách mắc mạch an toàn.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vôn kế là dụng cụ dùng để đo đại lượng vật lý nào trong mạch điện?',
                'Vôn kế (kí hiệu chữ V trên mặt đồng hồ) là thiết bị chuyên dùng để đo hiệu điện thế (điện áp) giữa hai điểm bất kỳ trong mạch điện.',
                'Đo lường 6 - Chức năng vôn kế', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Cường độ dòng điện (Ampe).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hiệu điện thế giữa hai điểm trong mạch điện (Vôn).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Điện trở của đoạn dây dẫn (Ohm).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Công suất tiêu thụ điện (Watt).', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Để đo hiệu điện thế giữa hai đầu một bóng đèn, vôn kế phải được mắc vào mạch như thế nào?',
                'Vôn kế có điện trở rất lớn nên phải được mắc song song với bóng đèn, chốt dương (+) nối về phía cực dương nguồn và chốt âm (-) nối về phía cực âm nguồn.',
                'Đo lường 6 - Cách mắc vôn kế', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Mắc nối tiếp với bóng đèn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Mắc song song với bóng đèn, chốt (+) về phía cực dương và chốt (-) về phía cực âm nguồn.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Mắc tùy ý bất kỳ vị trí nào mà không cần quan tâm đến cực tính.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Mắc nối tiếp giữa nguồn điện và công tắc.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Trên mặt đồng hồ của vôn kế có ghi chữ "V". Nếu kim chỉ thị đang dừng ở vạch số 6 thì giá trị hiệu điện thế đo được là:',
                'Kí hiệu "V" nghĩa là đơn vị đo là Vôn (Volt). Kim chỉ số 6 tương ứng với 6 V.',
                'Đo lường 6 - Đọc chỉ số vôn kế', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', '6 mA', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '6 A', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', '6 V', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', '60 V', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Giới hạn đo (GHĐ) của một vôn kế là gì?',
                'Giới hạn đo (GHĐ) là giá trị lớn nhất ghi trên thang đo của dụng cụ mà vôn kế có thể đo được chính xác và an toàn.',
                'Đo lường 6 - Giới hạn đo', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Giá trị nhỏ nhất ghi trên thang đo của dụng cụ.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Giá trị lớn nhất ghi trên thang đo của dụng cụ.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Khoảng cách giữa hai vạch chia độ liên tiếp.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Sai số tuyệt đối của phép đo.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Nếu mắc ngược chốt (+) và (-) của vôn kế kim vào mạch điện một chiều thì hiện tượng gì sẽ xảy ra?',
                'Khi mắc ngược cực trong mạch 1 chiều, lực từ tác dụng lên khung dây làm kim quay ngược về bên trái dưới vạch 0, có thể làm cong gãy kim chỉ thị.',
                'Đo lường 6 - An toàn khi đo', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Kim vôn kế quay ngược về phía dưới vạch số 0 và có thể làm hỏng kim chỉ thị.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vôn kế vẫn chỉ đúng giá trị vì cực tính không quan trọng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Bóng đèn trong mạch bị cháy đứt tóc đèn ngay lập tức.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vôn kế tự động chuyển thành ampe kế.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 5: ĐOÀN TÀU ĐIỆN TỪ TRƯỜNG (slug: tau-dien-tu-truong) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'tau-dien-tu-truong' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_tau-dien-tu-truong' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_tau-dien-tu-truong',
            'Quiz 3D: Đoàn Tàu Điện Từ Trường', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra nguyên lý lực từ Lorentz đẩy đoàn tàu nam châm pin trong ống lò xo đồng.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Trong mô hình đoàn tàu từ tính đơn giản (viên pin gắn 2 nam châm neodymium ở 2 đầu trượt trong lò xo đồng), nguồn cung cấp dòng điện là gì?',
                'Viên pin đóng vai trò là nguồn điện một chiều cấp dòng điện khép kín chạy qua các vòng dây đồng tiếp xúc với nam châm.',
                'Điện từ 9 - Nguồn động lực tàu pin', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Lò xo đồng tự phát ra dòng điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Viên pin (hiệu điện thế giữa cực dương và cực âm của pin).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Từ trường của Trái Đất tạo ra dòng điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không khí bị ion hóa bên trong ống đồng.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Lực nào đã đẩy viên pin và nam châm chuyển động tự hành xuyên qua lòng ống cuộn dây đồng?',
                'Dòng điện chạy qua đoạn ống đồng giữa hai nam châm chịu tác dụng của lực từ (lực điện từ / lực Lorentz) sinh ra bởi từ trường nam châm, đẩy cụm pin chuyển động theo định luật 3 Newton.',
                'Điện từ 9 - Lực từ Lorentz', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Lực ma sát trượt giữa nam châm và đồng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lực từ (lực điện từ) sinh ra do tương tác giữa dòng điện trong cuộn dây và từ trường của nam châm.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lực hấp dẫn kéo pin rơi về phía trước.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Áp suất khí nén tự tích tụ phía sau viên pin.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Để đoàn tàu pin có thể tự chạy được thì hai nam châm neodymium ở hai đầu pin phải được định hướng như thế nào?',
                'Cực từ của hai nam châm phải được định hướng chính xác (cùng cực đẩy hoặc hút tương ứng) để tổng hợp lực từ tác dụng lên hai đầu viên pin cùng hướng đẩy tàu về một phía.',
                'Điện từ 9 - Cực từ của nam châm', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Cực từ ở hai đầu phải được định hướng phù hợp để lực từ ở hai đầu pin cùng hướng về một phía.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai nam châm phải làm bằng chất cách điện tuyệt đối.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Bắt buộc phải lắp thêm động cơ cánh quạt mini ở đuôi pin.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không cần dùng nam châm, chỉ cần viên pin trượt tự do.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Tại sao ống lò xo làm đường hầm cho đoàn tàu pin phải được quấn bằng dây đồng trần (không có lớp men cách điện)?',
                'Nếu dây đồng có lớp men cách điện thì nam châm không thể tiếp xúc điện với cuộn dây, mạch điện không kín nên không có dòng điện và không sinh ra lực từ.',
                'Điện từ 9 - Mạch tiếp xúc', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Để cuộn dây đồng nhìn sáng bóng đẹp mắt hơn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để nam châm dẫn điện ở hai đầu pin có thể tiếp xúc trực tiếp tạo thành mạch điện kín qua các vòng dây.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để giảm bớt khối lượng của cuộn dây.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để tăng độ dẻo đàn hồi của lò xo đồng.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Ứng dụng thực tế hiện đại nhất của lực từ và đệm từ trường trong giao thông vận tải đường sắt là gì?',
                'Tàu đệm từ siêu tốc Maglev ứng dụng lực từ để nâng tàu lơ lửng trên đường ray và lực từ đẩy tàu chuyển động, loại bỏ hoàn toàn ma sát bánh xe để đạt tốc độ trên 600 km/h.',
                'Điện từ 9 - Ứng dụng tàu Maglev', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Tàu hỏa chạy bằng than đá cổ điển.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tàu đệm từ trường siêu tốc Maglev (Magnetic Levitation).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Xe đạp điện có gắn bình ắc quy.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Xe buýt công cộng chạy khí gas nén.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 6: BO MẠCH BREADBOARD (slug: breadboard-mach-dien) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'breadboard-mach-dien' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_breadboard-mach-dien' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_breadboard-mach-dien',
            'Quiz 3D: Bo Mạch Nối Dây (Breadboard)', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra cấu trúc kết nối ngầm bên trong breadboard và cách lắp ráp mạch thử nghiệm.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Công dụng chính của bo mạch thử nghiệm (breadboard) trong thực hành điện tử là gì?',
                'Breadboard cho phép cắm chân linh kiện và dây nối để kiểm thử mạch điện nhanh chóng, dễ tháo lắp thay đổi linh kiện mà không cần phải hàn chì cố định.',
                'Điện học 9 - Chức năng breadboard', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Dùng để hàn chết các linh kiện vĩnh viễn trên bản mạch in.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để cắm, lắp ráp và kiểm thử các mạch điện tử nhanh chóng, tái sử dụng linh kiện không cần hàn chì.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng làm nguồn cấp điện xoay chiều 220V.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng làm bộ chống sét lan truyền cho phòng thí nghiệm.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Tại khu vực cắm linh kiện chính (chia hai bên rãnh giữa), các lỗ cắm được nối ngầm với nhau theo quy tắc nào?',
                'Ở khu vực linh kiện (terminal strip), các lỗ cắm được kẹp chung một dải kim loại ngầm theo từng cột dọc gồm 5 lỗ (mỗi nhóm 5 lỗ là một nút mạch).',
                'Điện học 9 - Kết nối ngầm breadboard', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Nối thông nhau theo hàng ngang trên toàn bộ chiều dài bo mạch.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nối thông nhau theo từng cột dọc gồm 5 lỗ (mỗi dải 5 chân là một nút mạch).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tất cả các lỗ trên bo mạch đều nối thông kim loại với nhau.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Các lỗ hoàn toàn độc lập, không có lỗ nào nối ngầm với nhau.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Rãnh phân cách nằm dọc ở chính giữa bo mạch breadboard có tác dụng gì?',
                'Rãnh ở giữa cách ly hai bên, giúp cắm các vi mạch tích hợp (IC) dạng chân DIP sao cho hai hàng chân đối diện của IC không bị nối tắt với nhau.',
                'Điện học 9 - Cắm IC trên breadboard', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Dùng để cắm vừa vặn các vi mạch IC sao cho hai hàng chân đối diện không bị ngắn mạch với nhau.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để chứa nước làm mát bo mạch.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để chứa các ốc vít dư thừa.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chỉ có tác dụng trang trí thẩm mỹ.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Hai dải dài ở hai mép ngoài cùng của breadboard thường có vạch màu đỏ (+) và xanh/đen (-) dùng để làm gì?',
                'Dải cấp nguồn (power rails) chạy dọc suốt chiều dài bo mạch, giúp phân phối điện áp dương (+) và cực âm/GND (-) đến mọi nơi trên mạch thuận tiện.',
                'Điện học 9 - Dải nguồn breadboard', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Dải cấp nguồn (power rails) phân phối điện áp dương và mass cho toàn mạch.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dải đo nhiệt độ của bo mạch.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dải cắm cố định các biến trở công suất lớn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dải nối đất chống tĩnh điện cho tay người dùng.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi cắm cả hai chân của một điện trở vào cùng một cột 5 lỗ cắm trên breadboard thì mạch điện sẽ xảy ra hiện tượng gì?',
                'Vì 5 lỗ cùng 1 cột đã được nối thông kim loại bên dưới, cắm cả 2 chân điện trở vào cùng cột sẽ làm hai đầu điện trở bị ngắn mạch (nối tắt), dòng điện sẽ đi qua thanh kim loại thay vì qua điện trở.',
                'Điện học 9 - Lỗi ngắn mạch breadboard', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Giá trị điện trở tăng lên gấp đôi.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai đầu điện trở bị nối tắt (ngắn mạch), dòng điện không đi qua điện trở.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Điện trở sẽ phát sáng như đèn LED.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Mạch điện vẫn hoạt động bình thường như mong muốn.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 7: CẤU TẠO PIN ĐIỆN HÓA (slug: pin-dien-hoa) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'pin-dien-hoa' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_pin-dien-hoa' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_pin-dien-hoa',
            'Quiz 3D: Cấu Tạo Pin Điện Hóa', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra sự chuyển hóa hóa năng thành điện năng và cấu trúc điện cực trong pin.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Pin điện hóa là thiết bị thực hiện quá trình chuyển hóa dạng năng lượng nào thành điện năng?',
                'Pin điện hóa biến đổi trực tiếp hóa năng của các phản ứng hóa học (phản ứng oxi hóa - khử) thành điện năng.',
                'Điện học 9 - Chuyển hóa năng lượng của pin', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Quang năng thành điện năng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hóa năng thành điện năng.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cơ năng thành điện năng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nhiệt năng thành điện năng.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Cực âm (anode) của pin điện hóa là nơi diễn ra quá trình nào sau đây?',
                'Tại cực âm (anode), kim loại hoạt động hơn bị oxi hóa và giải phóng electron: M -> M(n+) + n.e, đưa electron chạy qua mạch ngoài sang cực dương.',
                'Điện học 9 - Điện cực anode', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Quá trình khử (nhận electron từ mạch ngoài).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quá trình oxi hóa (nhường electron vào mạch ngoài).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quá trình bay hơi nước.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quá trình trung hòa axit bazơ.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Màng ngăn xốp (separator) hoặc cầu muối trong pin điện hóa có vai trò gì?',
                'Màng ngăn ngăn cách hai điện cực không tiếp xúc cơ học gây ngắn mạch trực tiếp, nhưng vẫn cho phép các ion di chuyển qua lại để giữ trung hòa điện tích dung dịch.',
                'Điện học 9 - Màng ngăn điện tích', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Ngăn dòng electron chạy qua mạch ngoài.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ngăn hai cực chạm nhau gây chập mạch, đồng thời cho phép ion thẩm thấu để cân bằng điện tích.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tăng độ cứng cho vỏ pin.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đốt cháy khí thừa sinh ra bên trong pin.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Chất điện phân (electrolyte) bên trong pin có đặc điểm quan trọng nào?',
                'Chất điện phân chứa các ion dương và âm chuyển động tự do, đóng vai trò hạt tải điện dẫn điện trong lòng pin để khép kín mạch điện.',
                'Điện học 9 - Chất điện phân', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Là chất cách điện hoàn toàn không cho bất kỳ hạt tích điện nào đi qua.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chứa các ion tự do có khả năng dẫn điện bằng sự chuyển động của các hạt mang điện.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Phải luôn là kim loại rắn nguyên chất.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Là chất khí trơ không tham gia phản ứng.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi một viên pin dùng một lần bị hết điện (cạn pin), nguyên nhân cốt lõi là gì?',
                'Khi các chất phản ứng hóa học ở hai điện cực bị tiêu hao hết hoặc phản ứng đạt trạng thái cân bằng, hiệu điện thế giữa hai cực giảm về 0 và pin ngừng sinh dòng.',
                'Điện học 9 - Cạn kiệt hóa năng', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Toàn bộ dây dẫn bị đứt ngầm bên trong.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Các chất phản ứng hóa học ở điện cực đã phản ứng hết hoặc hệ đạt trạng thái cân bằng hóa học.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Khối lượng của viên pin bị giảm về 0.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Do nhiệt độ môi trường giảm xuống điểm đóng băng.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 8: TỪ TRƯỜNG NAM CHÂM CHỮ U (slug: nam-cham-mong-ngua) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'nam-cham-mong-ngua' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_nam-cham-mong-ngua' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_nam-cham-mong-ngua',
            'Quiz 3D: Từ Trường Nam Châm Chữ U', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra từ phổ, chiều đường sức từ và vùng từ trường đều giữa hai cực nam châm chữ U.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Đường sức từ ở khoảng không gian bên ngoài nam châm chữ U có chiều quy ước như thế nào?',
                'Quy ước chiều của đường sức từ bên ngoài nam châm là: Đi ra từ cực Bắc (North - N) và đi vào cực Nam (South - S) ("Vào Nam, Ra Bắc").',
                'Từ học 9 - Chiều đường sức từ', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Đi ra từ cực Nam (S) và đi vào cực Bắc (N).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đi ra từ cực Bắc (N) và đi vào cực Nam (S).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Xuất phát từ giữa thân nam châm lan tỏa tròn ra xung quanh.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không có quy ước về chiều đường sức từ.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vùng không gian nào quanh nam châm chữ U có từ trường đều (các đường sức từ là những đoạn thẳng song song và cách đều nhau)?',
                'Ở khoảng không gian hẹp giữa hai cực từ Bắc và Nam của nam châm chữ U, từ trường rất mạnh và gần như là từ trường đều.',
                'Từ học 9 - Vùng từ trường đều', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Ở phần lưng uốn cong của nam châm.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ở khoảng không gian hẹp giữa hai cực từ Bắc và Nam của nam châm.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ở khoảng cách rất xa hai cực của nam châm.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nam châm chữ U không có vùng từ trường đều ở bất cứ đâu.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Nếu ta cưa đôi một thanh nam châm chữ U ngay tại khúc uốn cong ở giữa thì ta sẽ thu được kết quả gì?',
                'Trong tự nhiên không tồn tại đơn cực từ. Khi bẻ đôi nam châm, tại mỗi vết cắt sẽ lập tức xuất hiện cực từ đối diện, tạo thành hai nam châm hoàn chỉnh mới.',
                'Từ học 9 - Cực từ của nam châm', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Một nửa chỉ có cực Bắc duy nhất và một nửa chỉ có cực Nam duy nhất.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai thanh nam châm mới, mỗi thanh đều có đầy đủ cả hai cực Bắc và Nam.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai khối sắt bình thường mất hoàn toàn từ tính.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Một thanh mang điện tích dương và một thanh mang điện tích âm.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi rắc mạt sắt xung quanh nam châm chữ U rồi gõ nhẹ lên tấm bìa phẳng, hình ảnh các đường mạt sắt định hướng được gọi là gì?',
                'Hình ảnh các đường mạt sắt sắp xếp xung quanh nam châm được gọi là từ phổ, giúp ta quan sát trực quan hình dạng của các đường sức từ.',
                'Từ học 9 - Từ phổ', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Phổ quang học.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Từ phổ.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Điện phổ.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Sóng âm.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Lực từ của nam châm có thể hút mạnh các vật liệu làm từ chất nào sau đây?',
                'Nam châm hút mạnh các vật liệu từ tính (chất sắt từ) như sắt (Fe), thép, niken (Ni), coban (Co).',
                'Từ học 9 - Vật liệu sắt từ', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Đồng (Cu).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nhôm (Al).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Sắt (Fe), thép và niken (Ni).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vàng (Au) và bạc (Ag).', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 9: TỪ TRƯỜNG CUỘN DÂY SOLENOID (slug: tu-truong-cuon-day-solenoid) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'tu-truong-cuon-day-solenoid' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_tu-truong-cuon-day-solenoid' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_tu-truong-cuon-day-solenoid',
            'Quiz 3D: Từ Trường Ống Dây Solenoid', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra từ phổ ống dây có dòng điện, quy tắc nắm tay phải và từ trường đều trong lòng solenoid.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Phổ đường sức từ ở BÊN NGOÀI ống dây có dòng điện chạy qua giống với từ phổ của dụng cụ nào?',
                'Ở bên ngoài ống dây có dòng điện, các đường sức từ là những đường cong khép kín xuất phát từ một đầu và đi vào đầu kia, giống hệt từ phổ của một thanh nam châm thẳng.',
                'Điện từ 9 - Từ phổ solenoid ngoài', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Một quả cầu tích điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Một thanh nam châm thẳng.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Một dây dẫn thẳng dài vô hạn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Một nam châm hình móng ngựa.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Đặc điểm nổi bật của các đường sức từ ở BÊN TRONG lòng ống dây dài có dòng điện là gì?',
                'Trong lòng ống dây dài có dòng điện, từ trường là từ trường đều; các đường sức từ gần như là những đoạn thẳng song song, cùng chiều và cách đều nhau.',
                'Điện từ 9 - Từ trường trong lòng ống dây', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Là các đường xoắn ốc hỗn loạn không có quy luật.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Gần như là những đoạn thẳng song song, cùng chiều và cách đều nhau (từ trường đều).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tập trung hội tụ về đúng tâm điểm của ống dây.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Trong lòng ống dây hoàn toàn không có đường sức từ.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Để xác định chiều đường sức từ (hoặc cực từ Bắc - Nam) của ống dây có dòng điện, người ta sử dụng quy tắc nào?',
                'Quy tắc nắm tay phải: Nắm bàn tay phải sao cho 4 ngón tay chỉ theo chiều dòng điện chạy qua các vòng dây, khi đó ngón tay cái choãi ra chỉ chiều của đường sức từ trong lòng ống dây (chỉ về cực Bắc).',
                'Điện từ 9 - Quy tắc nắm tay phải', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Quy tắc bàn tay trái.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quy tắc nắm tay phải.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quy tắc vặn đinh ốc ngược.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Quy tắc đòn bẩy.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi đảo ngược chiều dòng điện chạy qua các vòng dây của ống dây solenoid thì điều gì sẽ xảy ra?',
                'Chiều của đường sức từ phụ thuộc vào chiều dòng điện. Khi đổi chiều dòng điện, các cực từ ở hai đầu ống dây cũng hoán đổi vị trí (cực Bắc thành cực Nam và ngược lại).',
                'Điện từ 9 - Đổi chiều dòng điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Độ lớn từ trường tăng vọt lên 10 lần.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chiều của từ trường và tên các cực từ ở hai đầu ống dây sẽ bị đổi chiều ngược lại.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ống dây mất hoàn toàn từ tính.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ống dây bị nóng chảy ngay lập tức.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Biện pháp nào sau đây làm tăng độ mạnh của từ trường trong lòng một ống dây có dòng điện?',
                'Từ trường bên trong ống dây tỉ lệ thuận với mật độ số vòng dây quấn và cường độ dòng điện (B ~ n.I). Do đó tăng dòng điện và tăng số vòng dây quấn sẽ làm từ trường mạnh hơn.',
                'Điện từ 9 - Tăng cường độ từ trường', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Tăng cường độ dòng điện chạy qua cuộn dây và tăng số vòng dây quấn trên một đơn vị chiều dài.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Giảm cường độ dòng điện về gần bằng 0.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tháo bớt số vòng dây của cuộn dây.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng dây dẫn có vỏ bọc dày hơn.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 10: ĐỒNG HỒ VẠN NĂNG (slug: dong-ho-van-nang) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'dong-ho-van-nang' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_dong-ho-van-nang' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_dong-ho-van-nang',
            'Quiz 3D: Đồng Hồ Vạn Năng', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra các chức năng đo V-A-Ohm của đồng hồ VOM và an toàn thực hành.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Đồng hồ đo điện vạn năng (VOM) có thể đo được những đại lượng vật lý cơ bản nào?',
                'Tên gọi VOM là viết tắt của Volt - Ohm - Milliampere. Đồng hồ vạn năng có thể đo hiệu điện thế (V), điện trở (Ω) và cường độ dòng điện (A).',
                'Đo lường 9 - Chức năng đồng hồ VOM', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Chỉ đo được tốc độ quay của động cơ điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hiệu điện thế (Vôn), cường độ dòng điện (Ampe) và điện trở (Ohm).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chỉ đo được thể tích và khối lượng của dây dẫn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đo công suất phát sáng của bóng đèn sợi đốt.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi muốn đo giá trị điện trở của một linh kiện bằng đồng hồ vạn năng, bước an toàn bắt buộc đầu tiên là gì?',
                'Khi đo điện trở (thang đo Ohm), đồng hồ dùng pin bên trong để cấp dòng điện nhỏ qua linh kiện. Nếu mạch còn nối điện, điện áp ngoài sẽ làm cháy hỏng đồng hồ đo.',
                'Đo lường 9 - Quy tắc an toàn đo điện trở', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Bật nguồn điện tối đa để kiểm tra độ chịu nhiệt.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ngắt linh kiện ra khỏi nguồn điện của mạch trước khi đo.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ngâm linh kiện vào nước muối loãng để tăng dẫn điện.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chập hai que đo vào nguồn điện lưới 220V.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Ký hiệu "DCV" (hoặc chữ V kèm dấu gạch ngang thẳng) trên núm xoay của đồng hồ vạn năng biểu thị chức năng gì?',
                'DCV là viết tắt của Direct Current Voltage, dùng để đo hiệu điện thế của dòng điện một chiều (như pin, ắc quy).',
                'Đo lường 9 - Thang đo DCV', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Đo hiệu điện thế một chiều (Direct Current Voltage).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đo hiệu điện thế xoay chiều của điện lưới gia đình.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đo cường độ dòng điện xoay chiều.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đo nhiệt độ môi trường xung quanh.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Trước khi đo một điện áp chưa biết rõ khoảng giá trị, ta nên chọn thang đo trên đồng hồ vạn năng như thế nào để bảo vệ thiết bị?',
                'Chọn thang đo có giới hạn lớn nhất giúp tránh trường hợp điện áp thực tế vượt quá thang đo làm quá tải cháy cuộn dây hoặc nổ cầu chì bảo vệ.',
                'Đo lường 9 - Chọn thang đo an toàn', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Chọn thang đo có giới hạn nhỏ nhất để đọc rõ số lẻ.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chọn thang đo có giới hạn lớn nhất, sau đó hạ dần thang đo nếu cần để có kết quả chính xác hơn.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chọn thang đo Ohm đo điện trở.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không cần xoay núm thang đo.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi cắm que đo vào đồng hồ vạn năng, quy ước cắm màu que đo chuẩn là:',
                'Cổng "COM" (Common - cực âm/mass) luôn dành cho que đo màu đen; que đo màu đỏ cắm vào cổng tương ứng cần đo (V/Ω/mA).',
                'Đo lường 9 - Quy ước que đo', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Que đỏ cắm cổng COM, que đen cắm cổng V/Ω.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Que đen cắm vào cổng chung COM (âm), que đỏ cắm vào cổng V/Ω/mA (dương).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cả hai que cắm chung vào một lỗ duy nhất.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cắm tùy tiện bất kỳ lỗ nào không ảnh hưởng gì.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 11: CUỘN DÂY ĐIỆN SOLENOID (slug: cuon-day-solenoid) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'cuon-day-solenoid' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_cuon-day-solenoid' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_cuon-day-solenoid',
            'Quiz 3D: Cuộn Dây Điện Solenoid', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra cấu tạo nam châm điện, lõi sắt non và ứng dụng rơ le điện từ.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Cấu tạo cơ bản của một cuộn dây điện solenoid gồm có những bộ phận nào?',
                'Cuộn dây solenoid là ống dây hình trụ gồm nhiều vòng dây dẫn kim loại có vỏ cách điện quấn khít nhau quanh một lõi hình trụ.',
                'Điện từ 9 - Cấu tạo solenoid', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Một thanh kim loại đặc không có dây quấn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Ống dây dẫn hình trụ được quấn nhiều vòng dây kim loại cách điện xếp khít nhau.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Hai tấm nhôm phẳng đặt song song trong chân không.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Một sợi dây cao su dẫn nhiệt.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi đặt một lõi sắt non vào trong lòng một cuộn dây solenoid có dòng điện chạy qua thì:',
                'Lõi sắt non có độ từ thẩm rất lớn, khi đặt vào lòng ống dây nó bị nhiễm từ và khuếch đại tác dụng từ của cuộn dây lên hàng trăm lần, tạo thành nam châm điện mạnh.',
                'Điện từ 9 - Tác dụng lõi sắt non', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Từ trường của cuộn dây bị triệt tiêu hoàn toàn.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lõi sắt non bị nhiễm từ và làm tăng tác dụng từ của cuộn dây lên rất nhiều lần (tạo thành nam châm điện).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dòng điện trong cuộn dây lập tức dừng lại.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Lõi sắt non biến thành chất cách điện.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Ưu điểm vượt trội của nam châm điện (solenoid có lõi sắt) so với nam châm vĩnh cửu là gì?',
                'Nam châm điện có thể điều khiển linh hoạt: bật/tắt từ tính dễ dàng bằng cách đóng/ngắt mạch, thay đổi lực hút bằng cách chỉnh cường độ dòng điện, và đổi cực từ bằng cách đổi chiều dòng điện.',
                'Điện từ 9 - Ưu điểm nam châm điện', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Nam châm điện nhẹ hơn nam châm vĩnh cửu rất nhiều.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Có thể bật, tắt từ tính dễ dàng bằng dòng điện và có thể điều chỉnh độ mạnh yếu bằng cường độ dòng điện.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Không bao giờ bị nóng lên khi hoạt động liên tục.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Có thể hút được cả vàng và nhựa.', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vì sao người ta thường dùng lõi sắt non thay vì lõi thép để chế tạo nam châm điện dùng trong cần cẩu phế liệu?',
                'Sắt non nhiễm từ nhanh và mất từ tính gần như ngay lập tức khi ngắt dòng điện, giúp cần cẩu nhả phế liệu dễ dàng. Thép giữ từ tính lâu thành nam châm vĩnh cửu, không thể nhả phế liệu.',
                'Điện từ 9 - Sắt non so với thép', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Vì thép không bị nhiễm từ.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vì sắt non nhiễm từ nhanh và mất từ tính ngay khi ngắt điện, còn thép giữ từ tính lâu khó nhả phế liệu.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vì sắt non đắt hơn thép rất nhiều.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Vì thép dễ vỡ vụn hơn sắt non.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Thiết bị nào sau đây sử dụng cuộn hút solenoid để đóng/ngắt mạch điện tự động điều khiển từ xa?',
                'Rơ le điện từ (relay) sử dụng cuộn hút solenoid: khi có tín hiệu dòng điện nhỏ chạy qua cuộn dây, lực từ hút tiếp điểm cơ khí đóng/mở mạch điện công suất lớn.',
                'Điện từ 9 - Ứng dụng Rơ le điện từ', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Rơ le điện từ (Electromagnetic Relay).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Bếp ga mini du lịch.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đèn sợi đốt Vonfram.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Thước kẻ nhựa.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

    -- ==========================================================================
    -- MÔ HÌNH 12: VÔN KẾ THÍ NGHIỆM TIÊU CHUẨN (slug: von-ke-chi-tiet) - LỚP 9
    -- ==========================================================================
    SELECT id INTO v_model_id FROM bio_models WHERE slug = 'von-ke-chi-tiet' LIMIT 1;
    SELECT id INTO v_exam_id FROM exam WHERE code = 'QUIZ_MODEL_von-ke-chi-tiet' LIMIT 1;

    IF v_exam_id IS NULL THEN
        INSERT INTO exam (model_id, subject_id, code, name, type, subject_name, description, duration_minutes, total_score, is_active, created_date, updated_date)
        VALUES (
            v_model_id, v_sub_id_9, 'QUIZ_MODEL_von-ke-chi-tiet',
            'Quiz 3D: Vôn Kế Thí Nghiệm Tiêu Chuẩn', 'DEFAULT', 'Vật lý',
            'Bộ câu hỏi trắc nghiệm tương tác kiểm tra cơ cấu khung quay từ điện, nút chỉnh zero và sai số thị sai khi đọc vôn kế phòng lab.',
            10, 10.0, TRUE, NOW(), NOW()
        ) RETURNING id INTO v_exam_id;

        -- Câu 1
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Cơ cấu đo khung quay bên trong vôn kế kim thí nghiệm hoạt động dựa trên tác dụng nào của dòng điện?',
                'Cơ cấu khung quay hoạt động dựa trên tác dụng từ: lực từ tác dụng lên cuộn dây mang dòng điện đặt trong từ trường của nam châm vĩnh cửu làm khung dây và kim chỉ thị quay.',
                'Đo lường 9 - Cơ cấu từ điện khung quay', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 1, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Tác dụng nhiệt làm nở kim loại.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tác dụng từ của dòng điện tác dụng lên khung dây đặt trong từ trường nam châm.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tác dụng hóa học làm phân hủy chất điện phân.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Tác dụng sinh lý của dòng điện.', FALSE, NOW(), NOW());

        -- Câu 2
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vít xoay nhỏ (nút chỉnh cơ khí) nằm ngay dưới trục quay của kim trên mặt vôn kế có chức năng gì?',
                'Vít chỉnh zero cơ khí giúp điều chỉnh lực căng của lò xo xoắn, đưa kim chỉ thị về đúng vạch số 0 trước khi đo để loại bỏ sai số hệ thống ban đầu.',
                'Đo lường 9 - Nút chỉnh zero', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 2, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Dùng để chỉnh kim chỉ thị về đúng vạch số 0 (Zero adjustment) trước khi đo.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để bật tắt nguồn điện bên trong vôn kế.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để thay đổi điện trở trong của vôn kế.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Dùng để khóa chặt kim không cho chuyển động.', FALSE, NOW(), NOW());

        -- Câu 3
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Một vôn kế có hai thang đo: 3V và 15V với các chốt tương ứng (-), (3V), (15V). Nếu đo nguồn điện dự kiến khoảng 9V thì phải cắm dây vào các chốt nào?',
                'Điện áp cần đo là 9V lớn hơn thang 3V (cắm thang 3V sẽ làm vọt kim quá tải hỏng vôn kế). Do đó bắt buộc phải cắm vào chốt âm (-) và chốt thang đo (15V).',
                'Đo lường 9 - Lựa chọn thang đo vôn kế', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 3, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Chốt (-) và chốt (3V).', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chốt (-) và chốt (15V).', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Cắm vào cả 3 chốt cùng lúc.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Chốt (3V) và chốt (15V).', FALSE, NOW(), NOW());

        -- Câu 4
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Vì sao một vôn kế lý tưởng phải có điện trở trong (Rv) vô cùng lớn?',
                'Khi Rv rất lớn, dòng điện rẽ nhánh qua vôn kế xấp xỉ bằng 0 (Iv ≈ 0), giúp việc mắc song song vôn kế không làm sụt áp hoặc thay đổi phân bố dòng điện của đoạn mạch cần khảo sát.',
                'Đo lường 9 - Điện trở trong vôn kế', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 4, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Để vôn kế tiêu thụ nhiều điện năng nhất có thể.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để dòng điện qua vôn kế gần như bằng 0, không làm ảnh hưởng đến hiệu điện thế thực tế của mạch.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để mạch điện nóng lên nhanh chóng.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Để bảo vệ các dây dẫn ngoài khỏi bị đứt.', FALSE, NOW(), NOW());

        -- Câu 5
        INSERT INTO question (type, content, explanation, description, created_date, updated_date)
        VALUES ('SINGLE_CHOICE', 'Khi đọc chỉ số của vôn kế kim có dải gương phản chiếu bên dưới cung chia độ, mắt phải đặt ở vị trí nào để tránh sai số do góc nhìn (thị sai)?',
                'Mắt phải nhìn vuông góc trực diện với mặt thước đo sao cho kim chỉ thị che khuất hoàn toàn bóng phản chiếu của chính nó trong gương bên dưới, triệt tiêu sai số thị sai (parallax error).',
                'Đo lường 9 - Tránh sai số thị sai', NOW(), NOW()) RETURNING id INTO v_q_id;
        INSERT INTO exam_question (exam_id, question_id, question_order, point, created_date, updated_date) VALUES (v_exam_id, v_q_id, 5, v_point, NOW(), NOW());
        INSERT INTO answer (question_id, type, content, is_correct, created_date, updated_date) VALUES
            (v_q_id, 'TEXT', 'Đặt mắt nghiêng 45 độ từ phía bên phải.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nhìn vuông góc sao cho kim chỉ thị che khuất bóng phản chiếu của chính nó trong dải gương.', TRUE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Nhìn nghiêng từ phía dưới lên trên.', FALSE, NOW(), NOW()),
            (v_q_id, 'TEXT', 'Đặt mắt càng xa đồng hồ càng tốt và nhìn chéo.', FALSE, NOW(), NOW());
    ELSE
        UPDATE exam SET model_id = v_model_id WHERE id = v_exam_id;
    END IF;

END $$;
