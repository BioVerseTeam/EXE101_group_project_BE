-- =========================================================================
-- ĐOẠN MÃ CẬP NHẬT KIỂU DỮ LIỆU ĐỂ TRÁNH LỖI OVERFLOW VARCHAR(255)
-- =========================================================================
ALTER TABLE question ALTER COLUMN explanation TYPE TEXT;
ALTER TABLE question ALTER COLUMN description TYPE TEXT;
ALTER TABLE answer ALTER COLUMN explanation TYPE TEXT;
ALTER TABLE answer ALTER COLUMN description TYPE TEXT;
ALTER TABLE exam ALTER COLUMN description TYPE TEXT;

-- =========================================================================
-- ĐOẠN MÃ INSERT DỮ LIỆU ĐỀ THI KHTN 6 (KẾT NỐI TRI THỨC - ĐỀ SỐ 1)
-- =========================================================================

-- 1. Đảm bảo Exam ID 1 tồn tại (Cập nhật tiêu đề môn học cho phù hợp)
INSERT INTO exam (id, code, type, created_date, updated_date, name, subject_name, description)
VALUES (1, 'EXAM001', 'DEFAULT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Đề thi học kì I - Đề số 1', 'Khoa học tự nhiên 6', 'Môn: Khoa học tự nhiên 6 – Kết nối tri thức')
    ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, subject_name = EXCLUDED.subject_name, description = EXCLUDED.description;

-- 2. Chèn dữ liệu bảng Question (39 câu hỏi)
INSERT INTO question (id, type, content, created_date, updated_date, explanation, description) VALUES
                                                                                                   (1, 'MULTIPLE_CHOICE', 'Lực là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Lực là tác dụng đẩy kéo của vật này lên vật khác.', 'Câu 1'),
                                                                                                   (2, 'MULTIPLE_CHOICE', 'Trên vỏ một hộp thịt có ghi 500g. Số liệu đó chỉ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Con số 500g trên hộp thịt chỉ khối lượng của thịt trong hộp.', 'Câu 2'),
                                                                                                   (3, 'MULTIPLE_CHOICE', 'Một chú robot có thể cười, nói và hành động như một con người. Vậy robot là vật sống hay vật không sống? Tại sao?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Robot là vật không sống vì không có khả năng trao đổi chất với môi trường, lớn lên và sinh sản', 'Câu 3'),
                                                                                                   (4, 'MULTIPLE_CHOICE', 'Đặt vật trên một mặt bàn nằm ngang, móc lực kế vào vật và kéo sao cho lực kế luôn song song với mặt bàn và vật trượt nhanh dần. Số chỉ của lực kế khi đó', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vật trượt trên mặt bàn → giữa vật và mặt bàn có lực ma sát trượt. Vật trượt nhanh dần → lực ma sát nhỏ hơn lực kéo → số chỉ của lực kế lớn hơn độ lớn của lực ma sát. Vậy số chỉ của lực kế lớn hơn độ lớn lực ma sát trượt tác dụng lên vật.', 'Câu 4'),
                                                                                                   (5, 'MULTIPLE_CHOICE', 'Trường hợp nào sau đây vật không bị biến dạng khi chịu tác dụng của lực?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trường hợp vật không bị biến dạng khi chịu tác dụng của lực là: Viên bị sắt bị búng và lăn về phía trước.', 'Câu 5'),
                                                                                                   (6, 'MULTIPLE_CHOICE', 'Trường hợp nào sau đây liên quan đến lực không tiếp xúc?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Giọt mưa đang rơi, giọt mưa chịu tác dụng của lực hút Trái Đất, lực này là lực không tiếp xúc.', 'Câu 6'),
                                                                                                   (7, 'MULTIPLE_CHOICE', 'Hãy biểu diễn lực sau: Lực kéo vật có phương nằm ngang, chiều từ trái sang phải và có độ lớn 2000 N (1 cm ứng với 500N)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Lực kéo có: + Phương nằm ngang, chiều từ trái sang phải. + Độ lớn của lực: Tỉ xích 1 cm ứng với 500 N Lực có độ lớn 2000 N ứng với chiều dài là: 2000 : 500 . 1 = 4 (cm) → Hình vẽ đúng là: Hình C.', 'Câu 7'),
                                                                                                   (8, 'MULTIPLE_CHOICE', 'Một bạn học sinh nặng 17kg. Trọng lượng bạn học sinh đó là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Bạn học sinh nặng 17kg: m = 17 (kg) Trọng lượng của bạn học sinh đó: P = 10 x m = 10 x 17 = 170 N', 'Câu 8'),
                                                                                                   (9, 'MULTIPLE_CHOICE', 'Cho hình vẽ sau, GHĐ và ĐCNN của thước là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Thước có: GHĐ: 20cm và ĐCNN: 10mm.', 'Câu 9'),
                                                                                                   (10, 'MULTIPLE_CHOICE', 'Người ta đổ một lượng nước vào một bình chia độ như hình vẽ bên. Thể tích của nước trong bình là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Bình chia độ trong hình vẽ có GHĐ là 500ml; ĐCNN là 20ml Từ mực nước ta đọc được thể tích nước trong bình là 240ml', 'Câu 10'),
                                                                                                   (11, 'MULTIPLE_CHOICE', 'Phép đổi đơn vị thời gian nào sau đây là đúng?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Phép đổi đơn vị thời gian đúng là: + 30 ngày = 720 giờ → A đúng. + 45 phút = 2700 giây → B sai. + 1 giờ 27 phút = 5220 giây → C sai. + 24 giờ = 1440 phút → D sai.', 'Câu 11'),
                                                                                                   (12, 'MULTIPLE_CHOICE', 'Trước một chiếc cầu có một biển báo giao thông ghi 10T (hình vẽ), con số 10T này có ý nghĩa gì?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Với biển nói trên, nếu khối lượng toàn bộ xe (khối lượng của xe cộng với khối lượng hàng hóa) vượt quá 10 tấn thì không được phép đi qua cầu.', 'Câu 12'),
                                                                                                   (13, 'MULTIPLE_CHOICE', 'Khi treo một vật theo phương thẳng đứng vào lực kế, ta thấy số chỉ của lực kế là 200N. Khối lượng của vật đó là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Khi treo một vật theo phương thẳng đứng vào lực kế, ta thấy số chỉ của lực kế là 200N Trọng lượng của vật là P = 200 N Lại có: P = 10 x m => m = P : 10 = 20 (kg)', 'Câu 13'),
                                                                                                   (14, 'MULTIPLE_CHOICE', 'Mặt đế giày dép thường xẻ các rãnh nhỏ có tác dụng gì:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trên đế giày, dép có xẻ rãnh gai giúp tăng ma sát giữa giày dép và mặt đường, giúp người dễ dàng di chuyển và không bị trượt ngã.', 'Câu 14'),
                                                                                                   (15, 'MULTIPLE_CHOICE', 'Giá trị nhiệt độ đo được theo thang nhiệt độ Kenvin là 293K. Hỏi theo thang nhiệt độ Farenhai, nhiệt độ đó có giá trị là bao nhiêu? Biết rằng mỗi độ trong thang nhiệt độ Kenvin bằng độ trong thang nhiệt độ Xenxiut và ứng với 273K.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '293K ứng với: 293 - 273 = 20oC Đổi: 20oC = 32oF + 20.1,8oF = 68oF', 'Câu 15'),
                                                                                                   (16, 'MULTIPLE_CHOICE', 'Vật nào dưới đây là vật sống?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vật sống là con chó.', 'Câu 16'),
                                                                                                   (17, 'MULTIPLE_CHOICE', 'Quá trình nào sau đây không thể hiện tính chất hóa học của chất?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Quá trình không thể hiện tính chất hóa học của chất là nước để lâu trong không khí bị biến mất vì đây là quá trình bay hơi của chất thể hiện tính chất vật lí.', 'Câu 17'),
                                                                                                   (18, 'MULTIPLE_CHOICE', 'Cho các hình ảnh sau. Hình ảnh thể hiện sự sôi là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Sự sôi là quá trình bay hơi diễn ra cả trên bề mặt và trong lòng chất lỏng. ⟹ Hình ảnh thể hiện sự sôi là: Hình 4.', 'Câu 18'),
                                                                                                   (19, 'MULTIPLE_CHOICE', 'Phát biểu đúng khi nói về không khí là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'A, B sai, vì không khí là hỗn hợp của nhiều khí. C sai. D đúng.', 'Câu 19'),
                                                                                                   (20, 'MULTIPLE_CHOICE', 'Vật liệu nào dưới đây dẫn điện?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vật loại dẫn điện là kim loại.', 'Câu 20'),
                                                                                                   (21, 'MULTIPLE_CHOICE', 'Trong các chất sau đây, chất nào không phải là nhiên liệu?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nhiên liệu là chất khi đốt cháy sẽ giải phóng năng lượng. ⟹ Đất không phải là nhiên liệu.', 'Câu 21'),
                                                                                                   (22, 'MULTIPLE_CHOICE', 'Trong các phát biểu dưới đây, có bao nhiêu phát biểu đúng? (1) Xăng, dễ bắt cháy nhưng xăng dễ bay hơi và dễ cháy hơn dầu. (2) Mọi nhiên liệu đều có thể tái tạo trong thời gian ngắn. (3) Than đá là nhiên liệu hóa thạch. (4) Nhiên liệu sinh học được hình thành từ các hợp chất có nguồn gốc sinh học.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '(1) đúng. (2) sai, vì nhiên liệu hóa thạch phải mất nhiều thời gian mới có thể tái tạo. (3) đúng. (4) đúng.', 'Câu 22'),
                                                                                                   (23, 'MULTIPLE_CHOICE', 'Cho các phát biểu sau: (1) Các loại vitamin là không cần thiết đối với cơ thể. (2) Cà rốt là loại thực phẩm giàu vitamin A. (3) Lương thực – thực phẩm là các chất đã qua chế biến. (4) Lương thực như gạo, ngô, khoai, sắn, … có chứa tinh bột. (5) Lương thực – thực phẩm không có hạn sử dụng và có thể sử dụng mãi mãi. Số phát biểu đúng là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '(1) sai, vitamin cần thiết cho quá trình trao đổi chất trong cơ thể. (2) đúng. (3) sai, lương thực, thực phẩm có loại đã qua chế biến hoặc chưa qua chế biến. (4) đúng. (5) sai, phải có cách bảo quản lương thực, thực phẩm phù hợp, an toàn.', 'Câu 23'),
                                                                                                   (24, 'MULTIPLE_CHOICE', 'Oxygen có tính chất nào sau đây?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Ở điều kiện thường, oxygen là khí không màu, không mùi, không vị, ít tan trong nước, nặng hơn không khí, duy trì sự cháy và sự sống', 'Câu 24'),
                                                                                                   (25, 'MULTIPLE_CHOICE', 'Các cây thép dùng trong xây dựng nhà cửa, cầu, cống được sản suất từ loại nguyên liệu nào sau đây?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Thép được sản xuất từ quặng sắt.', 'Câu 25'),
                                                                                                   (26, 'MULTIPLE_CHOICE', 'Cho các diễn biến sau : 1. Hình thành vách ngăn giữa các tế bào con 2. Phân chia chất tế bào 3. Phân chia nhân Sự phân chia tế bào thực vật diễn ra theo trình tự sớm muộn như thế nào?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Thứ tự các giai đoạn của quá trình phân chia của tế bào thực vật là: 3 – 2 – 1', 'Câu 26'),
                                                                                                   (27, 'MULTIPLE_CHOICE', 'Bào quan là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Bào quan là những cấu trúc thực hiện các chức năng nhất định của tế bào, VD: lục lạp, ti thể,..', 'Câu 27'),
                                                                                                   (28, 'MULTIPLE_CHOICE', 'Cho các đối tượng sau: miếng thịt lợn, chiếc bút, con gà, chiếc lá khô, cây rau ngót, chiếc kéo, mật ong, chai nước, chiếc bàn (các cây và con vật đưa ra đều đang sống). Nhóm đối tượng gồm toàn vật sống là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trong các đối tượng đưa ra, các vật sống gồm: con gà, cây rau ngót.', 'Câu 28'),
                                                                                                   (29, 'MULTIPLE_CHOICE', 'Điểm giống nhau của cơ thể đơn bào và cơ thể đa bào là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Điểm giống nhau của cơ thể đơn bào và cơ thể đa bào là được cấu tạo từ tế bào.', 'Câu 29'),
                                                                                                   (30, 'MULTIPLE_CHOICE', 'Thành phần nào không có ở cả tế bào động vật và thực vật', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vùng nhân là thành phần của tế bào nhân sơ, không có ở tế bào nhân thực (TB động vật, thực vật)', 'Câu 30'),
                                                                                                   (31, 'MULTIPLE_CHOICE', 'Quan sát các cơ quan dưới đây: Hệ tiêu hoá gồm các cơ quan nào?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '(1) Não: thuộc hệ thần kinh (2) Tim: có chức năng bơm máu, thuộc hệ tuần hoàn (3) dạ dày: có chức năng co bóp để nghiền nát, đảo trộn thức ăn,.. thuộc hệ tiêu hóa. (4) phổi: có chức năng trao đổi khí, thuộc hệ hô hấp. (5) thận, có vai trò lọc máu, thuộc hệ bài tiết. (6) ruột, có vai trò tiêu hóa và hấp thụ chất dinh dưỡng, thuộc hệ tiêu hóa. Vậy cơ quan (3), (6) thuộc hệ tiêu hóa.', 'Câu 31'),
                                                                                                   (32, 'MULTIPLE_CHOICE', 'Từ một tế bào ban đầu, trải qua k lần phân chia tạo 128 tế bào con, k có giá trị là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Ta có 128 = 27 → tế bào này trải qua 7 lần phân chia.', 'Câu 32'),
                                                                                                   (33, 'MULTIPLE_CHOICE', 'Vì sao cần phải phân loại thế giới sống?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Chúng ta cần phân loại thế giới sống để xác định vị trí của các loài sinh vật, giúp cho việc tìm ra chúng giữa các sinh vật trở nên dễ dàng hơn.', 'Câu 33'),
                                                                                                   (34, 'MULTIPLE_CHOICE', 'Công cụ nào không hữu ích trong việc xác định các đặc điểm của sinh vật khi xây dựng khoá lưỡng phân?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trong các dụng cụ trên thì kính viễn vọng không được dùng để xác định đặc điểm của sinh vật nên không sử dụng để xây dựng khóa lưỡng phân.', 'Câu 34'),
                                                                                                   (35, 'MULTIPLE_CHOICE', 'Dưới đây là khóa lưỡng phân phân loại 4 sinh vật: cá, thằn lằn, hổ và khỉ đột. Có mấy cặp đặc điểm được sử dụng?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Các cặp đặc điểm được sử dụng là di chuyển bằng vây/chi; cơ thể phủ vảy/lông; có đuôi/không đuôi.', 'Câu 35'),
                                                                                                   (36, 'MULTIPLE_CHOICE', 'Cho các đặc điểm sau: (1) Có hệ thần kinh. (2) Đa bào phức tạp. (3) Sống tự dưỡng. (4) Cơ thể phân hóa thành các mô và cơ quan. (5) Có hình thức sinh sản hữu tính. (6) Có khả năng di chuyển chủ động. Các đặc điểm có ở cả giới Thực vật và giới Động vật là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Các đặc điểm có ở cả giới Thực vật và giới Động vật là: (2), (4), (5). (1),(6) chỉ có ở động vật (3) chỉ có ở thực vật', 'Câu 36'),
                                                                                                   (37, 'MULTIPLE_CHOICE', 'Tên phổ thông của sinh vật là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Cách gọi tên sinh vật: Tên phổ thông là cách gọi phổ biến của loài có trong danh lục tra cứu. Tên khoa học là cách gọi tên một loài sinh vật theo tên chi/giống và tên loài. Tên địa phương là cách gọi truyền thống của người dân bản địa theo vùng miền, quốc gia.', 'Câu 37'),
                                                                                                   (38, 'MULTIPLE_CHOICE', '“Giúp vi khuẩn bám vào tế bào vật chủ” là vai trò của', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '“Giúp vi khuẩn bám vào tế bào vật chủ” là vai trò của lông.', 'Câu 38'),
                                                                                                   (39, 'MULTIPLE_CHOICE', 'Vật chất di truyền của một virus là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vật chất di truyền của một virus là ADN hoặc ARN. Chỉ chứa 1 trong 2 loại axit nucleic.', 'Câu 39');


-- 3. Ánh xạ Câu hỏi vào Exam số 1 (ExamQuestion)
INSERT INTO exam_question (id, point, question_order, created_date, updated_date, exam_id, question_id) VALUES
                                                                                                            (1, 1.0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
                                                                                                            (2, 1.0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 2),
                                                                                                            (3, 1.0, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 3),
                                                                                                            (4, 1.0, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 4),
                                                                                                            (5, 1.0, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 5),
                                                                                                            (6, 1.0, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 6),
                                                                                                            (7, 1.0, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 7),
                                                                                                            (8, 1.0, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 8),
                                                                                                            (9, 1.0, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 9),
                                                                                                            (10, 1.0, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 10),
                                                                                                            (11, 1.0, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 11),
                                                                                                            (12, 1.0, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 12),
                                                                                                            (13, 1.0, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 13),
                                                                                                            (14, 1.0, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 14),
                                                                                                            (15, 1.0, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 15),
                                                                                                            (16, 1.0, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 16),
                                                                                                            (17, 1.0, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 17),
                                                                                                            (18, 1.0, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 18),
                                                                                                            (19, 1.0, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 19),
                                                                                                            (20, 1.0, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 20),
                                                                                                            (21, 1.0, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 21),
                                                                                                            (22, 1.0, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 22),
                                                                                                            (23, 1.0, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 23),
                                                                                                            (24, 1.0, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 24),
                                                                                                            (25, 1.0, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 25),
                                                                                                            (26, 1.0, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 26),
                                                                                                            (27, 1.0, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 27),
                                                                                                            (28, 1.0, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 28),
                                                                                                            (29, 1.0, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 29),
                                                                                                            (30, 1.0, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 30),
                                                                                                            (31, 1.0, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 31),
                                                                                                            (32, 1.0, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 32),
                                                                                                            (33, 1.0, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 33),
                                                                                                            (34, 1.0, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 34),
                                                                                                            (35, 1.0, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 35),
                                                                                                            (36, 1.0, 36, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 36),
                                                                                                            (37, 1.0, 37, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 37),
                                                                                                            (38, 1.0, 38, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 38),
                                                                                                            (39, 1.0, 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 39);


-- 4. Tạo 4 Tùy chọn đáp án (Answer) cho mỗi Question (Tổng 156 bản ghi)
-- Định dạng: Đáp án đúng tương ứng với Kết quả đúng (is_correct = TRUE)
INSERT INTO answer (id, type, content, created_date, updated_date, explanation, description, is_correct, question_id) VALUES
-- Câu 1 (Đúng: D)
(1, 'TEXT', 'tác dụng hút của vật này lên vật khác.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 1),
(2, 'TEXT', 'tác dụng đỡ của vật này lên vật khác.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 1),
(3, 'TEXT', 'tác dụng đẩy (kéo) của lực này lên lực khác.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 1),
(4, 'TEXT', 'tác dụng đẩy (kéo) của vật này lên vật khác.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 1),
-- Câu 2 (Đúng: A)
(5, 'TEXT', 'khối lượng của thịt trong hộp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 2),
(6, 'TEXT', 'thể tích của cả hộp thịt.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 2),
(7, 'TEXT', 'thể tích của thịt trong hộp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 2),
(8, 'TEXT', 'khối lượng của cả hộp thịt.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 2),
-- Câu 3 (Đúng: B)
(9, 'TEXT', 'Robot là vật sống vì có thể cười, nói và hành động như một con người.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 3),
(10, 'TEXT', 'Robot là vật không sống vì không có khả năng trao đổi chất với môi trường, lớn lên và sinh sản.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 3),
(11, 'TEXT', 'Robot là vật không sống vì có thể hành động như một con người.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 3),
(12, 'TEXT', 'Robot vừa là vật sống, vừa là vật không sống, vì có thể cười, nói và hành động như một con người, nhưng không có khả năng trao đổi chất với môi trường, lớn lên và sinh sản.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 3),
-- Câu 4 (Đúng: C)
(13, 'TEXT', 'bằng độ lớn lực ma sát nghỉ tác dụng lên vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 4),
(14, 'TEXT', 'bằng độ lớn lực ma sát trượt tác dụng lên vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 4),
(15, 'TEXT', 'lớn hơn độ lớn lực ma sát trượt tác dụng lên vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 4),
(16, 'TEXT', 'nhỏ hơn độ lớn lực ma sát trượt tác dụng lên vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 4),
-- Câu 5 (Đúng: C)
(17, 'TEXT', 'Cửa kính bị vỡ khi bị và đập mạnh.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 5),
(18, 'TEXT', 'Đất xốp khi được cày xới cần thận.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 5),
(19, 'TEXT', 'Viên bị sắt bị búng và lăn về phía trước.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 5),
(20, 'TEXT', 'Tờ giấy bị nhàu khi ta vò nó lại.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 5),
-- Câu 6 (Đúng: C)
(21, 'TEXT', 'Vận động viên nâng tạ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 6),
(22, 'TEXT', 'Người dọn hàng đẩy thùng hàng trên sàn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 6),
(23, 'TEXT', 'Giọt mưa đang rơi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 6),
(24, 'TEXT', 'Bạn Na đóng đinh vào tường.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 6),
-- Câu 7 (Đúng: C)
(25, 'TEXT', 'Hình A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 7),
(26, 'TEXT', 'Hình B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 7),
(27, 'TEXT', 'Hình C', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 7),
(28, 'TEXT', 'Hình D', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 7),
-- Câu 8 (Đúng: B)
(29, 'TEXT', '17 N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 8),
(30, 'TEXT', '170 N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 8),
(31, 'TEXT', '1700 N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 8),
(32, 'TEXT', '17000N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 8),
-- Câu 9 (Đúng: B)
(33, 'TEXT', 'GHĐ là 20cm và ĐCNN là 20mm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 9),
(34, 'TEXT', 'GHĐ là 20cm và ĐCNN là 10mm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 9),
(35, 'TEXT', 'GHĐ là 20cm và ĐCNN là 10cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 9),
(36, 'TEXT', 'GHĐ là 20cm và ĐCNN là 2cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 9),
-- Câu 10 (Đúng: B)
(37, 'TEXT', '200ml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 10),
(38, 'TEXT', '240ml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 10),
(39, 'TEXT', '220ml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 10),
(40, 'TEXT', '230ml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 10),
-- Câu 11 (Đúng: A)
(41, 'TEXT', '30 ngày = 720 giờ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 11),
(42, 'TEXT', '45 phút = 162000 giây.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 11),
(43, 'TEXT', '1 giờ 27 phút = 127000 giây.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 11),
(44, 'TEXT', '24 giờ = 720 phút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 11),
-- Câu 12 (Đúng: B)
(45, 'TEXT', 'Xe có trên 10 người ngồi thì không được đi qua cầu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 12),
(46, 'TEXT', 'Khối lượng toàn bộ (của cả xe và hàng) trên 10 tấn thì không được đi qua cầu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 12),
(47, 'TEXT', 'Khối lượng của xe trên 100 tấn thì không được đi qua cầu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 12),
(48, 'TEXT', 'Xe có khối lượng trên 10 tạ thì không được đi qua cầu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 12),
-- Câu 13 (Đúng: A)
(49, 'TEXT', '20kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 13),
(50, 'TEXT', '200g', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 13),
(51, 'TEXT', '200kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 13),
(52, 'TEXT', '2kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 13),
-- Câu 14 (Đúng: A)
(53, 'TEXT', 'Tăng ma sát để chống trơn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 14),
(54, 'TEXT', 'Giảm ma sát để chống trơn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 14),
(55, 'TEXT', 'Tiết kiệm nguyên vật liệu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 14),
(56, 'TEXT', 'Mẫu mã đẹp hơn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 14),
-- Câu 15 (Đúng: C)
(57, 'TEXT', '20oF.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 15),
(58, 'TEXT', '100 oF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 15),
(59, 'TEXT', '68 oF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 15),
(60, 'TEXT', '261 oF.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 15),
-- Câu 16 (Đúng: D)
(61, 'TEXT', 'Cây bút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 16),
(62, 'TEXT', 'Con dao.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 16),
(63, 'TEXT', 'Cây chổi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 16),
(64, 'TEXT', 'Con chó.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 16),
-- Câu 17 (Đúng: C)
(65, 'TEXT', 'Cơm để lâu trong không khí bị ôi, thiu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 17),
(66, 'TEXT', 'Sắt để lâu trong không khí bị gỉ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 17),
(67, 'TEXT', 'Nước để lâu trong không khí bị biến mất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 17),
(68, 'TEXT', 'Đun nóng đường trên chảo quá nóng sinh ra chất có màu đen.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 17),
-- Câu 18 (Đúng: D)
(69, 'TEXT', 'Hình 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 18),
(70, 'TEXT', 'Hình 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 18),
(71, 'TEXT', 'Hình 3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 18),
(72, 'TEXT', 'Hình 4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 18),
-- Câu 19 (Đúng: D)
(73, 'TEXT', 'Không khí là một đơn chất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 19),
(74, 'TEXT', 'Không khí là một nguyên tố hóa học.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 19),
(75, 'TEXT', 'Không khí là một hỗn hợp của nhiều nguyên tố trong đó chủ yếu là oxygen và nitrogen.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 19),
(76, 'TEXT', 'Không khí là hỗn hợp của nhiều khí trong đó chủ yếu là khí oxi và nitơ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 19),
-- Câu 20 (Đúng: A)
(77, 'TEXT', 'Kim loại.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 20),
(78, 'TEXT', 'Nhựa.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 20),
(79, 'TEXT', 'Gốm sứ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 20),
(80, 'TEXT', 'Cao su.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 20),
-- Câu 21 (Đúng: D)
(81, 'TEXT', 'Than.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 21),
(82, 'TEXT', 'Dầu.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 21),
(83, 'TEXT', 'Củi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 21),
(84, 'TEXT', 'Đất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 21),
-- Câu 22 (Đúng: B)
(85, 'TEXT', '2.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 22),
(86, 'TEXT', '3.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 22),
(87, 'TEXT', '4.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 22),
(88, 'TEXT', '5.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 22),
-- Câu 23 (Đúng: B)
(89, 'TEXT', '1.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 23),
(90, 'TEXT', '2.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 23),
(91, 'TEXT', '3.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 23),
(92, 'TEXT', '4.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 23),
-- Câu 24 (Đúng: B)
(93, 'TEXT', 'Ở điều kiện thường, oxygen là khí không màu, không mùi, không vị, ít tan trong nước, nặng hơn không khí, không duy trì sự cháy.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 24),
(94, 'TEXT', 'Ở điều kiện thường, oxygen là khí không màu, không mùi, không vị, ít tan trong nước, nặng hơn không khí, duy trì sự cháy và sự sống.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 24),
(95, 'TEXT', 'Ở điều kiện thường, oxygen là khí không màu, không mùi, không vị, ít tan trong nước, nhẹ hơn không khí, duy trì sự cháy và sự sống.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 24),
(96, 'TEXT', 'Ở điều kiện thường, oxygen là khí không màu, không mùi, không vị, tan nhiều trong nước, nặng hơn không khí, duy trì sự cháy và sự sống', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 24),
-- Câu 25 (Đúng: D)
(97, 'TEXT', 'Quặng bauxite', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 25),
(98, 'TEXT', 'Quặng đồng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 25),
(99, 'TEXT', 'Quặng chứa phosphorus.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 25),
(100, 'TEXT', 'Quặng sắt.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 25),
-- Câu 26 (Đúng: D)
(101, 'TEXT', '3 – 1 – 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 26),
(102, 'TEXT', '2 – 3– 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 26),
(103, 'TEXT', '1 – 2 – 3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 26),
(104, 'TEXT', '3 – 2 – 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 26),
-- Câu 27 (Đúng: C)
(105, 'TEXT', 'Các chất hóa học có trong tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 27),
(106, 'TEXT', 'Các phân tử hữu cơ có nằm trong tế bào chất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 27),
(107, 'TEXT', 'Những cấu trúc thực hiện các chức năng nhất định của tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 27),
(108, 'TEXT', 'Gồm các cấu trúc cơ bản của tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 27),
-- Câu 28 (Đúng: D)
(109, 'TEXT', 'Miếng thịt lợn, con gà, chiếc lá khô', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 28),
(110, 'TEXT', 'Cây rau ngót, con gà, chiếc bàn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 28),
(111, 'TEXT', 'Chiếc lá khô, chai nước, chiếc kéo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 28),
(112, 'TEXT', 'Con gà, cây rau ngót.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 28),
-- Câu 29 (Đúng: B)
(113, 'TEXT', 'Đều được cấu tạo từ tế bào nhân thực.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 29),
(114, 'TEXT', 'Đều được cấu tạo từ tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 29),
(115, 'TEXT', 'Đều là vật không sống.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 29),
(116, 'TEXT', 'Đơn vị cấu tạo nên cơ thể gồm 4 thành phần: nhân, tế bào chất, màng sinh chất, thành tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 29),
-- Câu 30 (Đúng: C)
(117, 'TEXT', 'Màng tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 30),
(118, 'TEXT', 'Thành tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 30),
(119, 'TEXT', 'Vùng nhân', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 30),
(120, 'TEXT', 'Nhân tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 30),
-- Câu 31 (Đúng: D)
(121, 'TEXT', '(2), (3)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 31),
(122, 'TEXT', '(3), (4)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 31),
(123, 'TEXT', '(3), (5).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 31),
(124, 'TEXT', '(3), (6)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 31),
-- Câu 32 (Đúng: B)
(125, 'TEXT', '6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 32),
(126, 'TEXT', '7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 32),
(127, 'TEXT', '8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 32),
(128, 'TEXT', '9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 32),
-- Câu 33 (Đúng: C)
(129, 'TEXT', 'Để đặt và gọi tên các loài sinh vật khi cần thiết.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 33),
(130, 'TEXT', 'Để xác định số lượng các loài sinh vật trên Trái Đất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 33),
(131, 'TEXT', 'Để xác định vị trí của các loài sinh vật, giúp cho việc tìm ra chúng giữa các sinh vật trở nên dễ dàng hơn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 33),
(132, 'TEXT', 'Để thấy được sự khác nhau giữa các loài sinh vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 33),
-- Câu 34 (Đúng: B)
(133, 'TEXT', 'Kính lúp cầm tay.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 34),
(134, 'TEXT', 'Kính viễn vọng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 34),
(135, 'TEXT', 'Kính hiển vi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 34),
(136, 'TEXT', 'Thước mét.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 34),
-- Câu 35 (Đúng: B)
(137, 'TEXT', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 35),
(138, 'TEXT', '3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 35),
(139, 'TEXT', '4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 35),
(140, 'TEXT', '5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 35),
-- Câu 36 (Đúng: C)
(141, 'TEXT', '(2), (5), (6)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 36),
(142, 'TEXT', '(1), (3), (4), (6).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 36),
(143, 'TEXT', '(2), (4), (5).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 36),
(144, 'TEXT', '(1), (2), (3), (4), (5).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 36),
-- Câu 37 (Đúng: A)
(145, 'TEXT', 'Cách gọi phổ biến trong danh lục tra cứu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 37),
(146, 'TEXT', 'Cách gọi tên một loài sinh vật theo tên chi/giống và tên loài.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 37),
(147, 'TEXT', 'Cách gọi truyền thống của người dân bản địa theo vùng miền, quốc gia.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 37),
(148, 'TEXT', 'Tất cả các phương án trên', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 37),
-- Câu 38 (Đúng: B)
(149, 'TEXT', 'Roi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 38),
(150, 'TEXT', 'Lông', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 38),
(151, 'TEXT', 'Thành tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 38),
(152, 'TEXT', 'Màng sinh chất', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 38),
-- Câu 39 (Đúng: D)
(153, 'TEXT', 'ARN và ADN.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 39),
(154, 'TEXT', 'ARN và gai glycoprotein.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 39),
(155, 'TEXT', 'ADN hoặc gai glycoprotein.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 39),
(156, 'TEXT', 'ADN hoặc ARN.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 39);


-- 5. Tạo liên kết Hình ảnh cho Câu hỏi (QuestionImage) theo URL từ file
INSERT INTO question_image (id, name, display_order, created_date, url, question_id) VALUES
                                                                                         (1, 'q_img_7.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/11.png', 7),
                                                                                         (2, 'q_img_9.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/12.png', 9),
                                                                                         (3, 'q_img_10.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/13.png', 10),
                                                                                         (4, 'q_img_12.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/14.png', 12),
                                                                                         (5, 'q_img_14.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/15.png', 14),
                                                                                         (6, 'q_img_18.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/16.png', 18),
                                                                                         (7, 'q_img_31.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/17.png', 31),
                                                                                         (8, 'q_img_35.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/18.png', 35);
-- Lưu ý: URL câu hỏi 40 bị lược bỏ do danh sách trích xuất chỉ có tới câu 39.


-- 6. Đồng bộ lại Database Sequences để không bị xung đột Auto-increment sau này
SELECT setval('exam_id_seq', (SELECT MAX(id) FROM exam));
SELECT setval('question_id_seq', (SELECT MAX(id) FROM question));
SELECT setval('exam_question_id_seq', (SELECT MAX(id) FROM exam_question));
SELECT setval('answer_id_seq', (SELECT MAX(id) FROM answer));
SELECT setval('question_image_id_seq', (SELECT MAX(id) FROM question_image));

-- =========================================================================
-- ĐOẠN MÃ INSERT DỮ LIỆU ĐỀ THI KHTN 6 (KẾT NỐI TRI THỨC - ĐỀ SỐ 2)
-- =========================================================================

-- 1. Cập nhật thông tin Exam ID 2 cho khớp với tiêu đề đề bài
INSERT INTO exam (id, code, type, created_date, updated_date, name, subject_name, description)
VALUES (2, 'EXAM002', 'DEFAULT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Đề thi học kì I – Đề số 2', 'Khoa học tự nhiên 6', 'Môn: Khoa học tự nhiên 6 – Kết nối tri thức')
    ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, subject_name = EXCLUDED.subject_name, description = EXCLUDED.description;

-- 2. Chèn dữ liệu bảng Question (40 câu hỏi từ Câu 1 đến Câu 40)
INSERT INTO question (id, type, content, created_date, updated_date, explanation, description) VALUES
                                                                                                   (61, 'MULTIPLE_CHOICE', 'Dụng cụ dùng để đo khối lượng của một vật là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Dụng cụ dùng để đo khối lượng của vật là cân.', 'Câu 1'),
                                                                                                   (62, 'MULTIPLE_CHOICE', 'Độ lớn của lực hút của Trái Đất tác dụng lên vật gọi là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trọng lượng là độ lớn của lực hút của Trái Đất tác dụng lên vật.', 'Câu 2'),
                                                                                                   (63, 'MULTIPLE_CHOICE', 'Trong các phát biểu sau đây, phát biểu nào đúng?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Phát biểu đúng là: Lực kế là dụng cụ để đo lực.', 'Câu 3'),
                                                                                                   (64, 'MULTIPLE_CHOICE', 'Hai bạn Nam và Hòa cùng đưa thùng hàng lên sàn ô tô (Nam đứng dưới đất còn Hòa đứng trên thùng xe). Nhận xét nào về lực tác dụng của Nam và Hòa lên thùng hàng sau đây là đúng?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nam đứng dưới đất còn Hòa đứng trên thùng xe nên Nam đẩy và Hòa kéo thùng hàng.', 'Câu 4'),
                                                                                                   (65, 'MULTIPLE_CHOICE', 'Hoạt động nào sau đây là hoạt động nghiên cứu khoa học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Hoạt động “Tìm hiểu vi khuẩn bằng kính hiển vi” và hoạt động “tìm hiểu vũ trụ” là hoạt động nghiên cứu khoa học.', 'Câu 5'),
                                                                                                   (66, 'MULTIPLE_CHOICE', 'Hành động nào sau đây không thực hiện đúng quy tắc an toàn trong phòng thực hành', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Đổ hóa chất vào cống thoát nước là hành động không thực hiện đúng quy tắc an toàn trong phòng thực hành.', 'Câu 6'),
                                                                                                   (67, 'MULTIPLE_CHOICE', 'Một vật đặt trên mặt bàn nằm ngang. Dùng tay búng vào vật để nó chuyển động. Vật sau đó chuyến động chậm dần vì có', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Một vật đặt trên mặt bàn nằm ngang. Dùng tay búng vào vật để nó chuyển động. Vật sau đó chuyến động chậm dần vì có lực ma sát.', 'Câu 7'),
                                                                                                   (68, 'MULTIPLE_CHOICE', 'Đổi các đơn vị đo nhiệt độ sau? a) 37oC = …… oF b) 50oF = …… oC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '37oC = 32 + 37 . 1,8 = 98,6oF; 50oF = (50 – 32) : 1,8 = 10oC', 'Câu 8'),
                                                                                                   (69, 'MULTIPLE_CHOICE', 'Thời gian giữa hai nhịp tim liên tiếp của người bình thường khoảng 0,8 s. Hỏi trong 1 phút, tim của một người bình thường đập bao nhiêu nhịp?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Đổi: 1 phút = 60 giây. Trong 1 phút, số nhịp đập của tim người bình thường là: 60 : 0,8 = 75 (nhịp)', 'Câu 9'),
                                                                                                   (70, 'MULTIPLE_CHOICE', 'Hãy diễn tả bằng lời phương, chiều và độ lớn của lực vẽ ở hình bên:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trong hình trên ta thấy mũi tên có phương nằm ngang, hướng từ trái sang phải. Tỉ xích 1 cm ứng với 10 N. Lực có độ dài 2 cm → độ lớn của lực là: 10 . 2 = 20 (N) → Lực của người đẩy thùng hàng có phương nằm ngang, chiều hướng từ trái sang phải, cường độ 20 N.', 'Câu 10'),
                                                                                                   (71, 'MULTIPLE_CHOICE', 'Người ta dùng búa để đóng một cái cọc tre xuống đất. Lực mà búa tác dụng lên cọc tre sẽ gây ra những kết quả gì?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Người ta dùng búa để đóng một cái cọc tre xuống đất. Lực mà búa tác dụng lên cọc tre vừa làm biến dạng cọc tre vừa làm biến đổi chuyển động của nó.', 'Câu 11'),
                                                                                                   (72, 'MULTIPLE_CHOICE', 'Trong các lực ở hình đầu bài, lực nào là lực tiếp xúc, lực nào là lực không tiếp xúc?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '+ Lực tiếp xúc: hình b; hình c; hình d + Lực không tiếp xúc: hình a.', 'Câu 12'),
                                                                                                   (73, 'MULTIPLE_CHOICE', 'Giải thích hiện tượng sau và cho biết trong hiện tượng này, ma sát có lợi hay có hại: Ô tô đi vào bùn dễ bị sa lầy.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Ô tô đi trên bùn dễ bị sa lầy vì lực ma sát giữa bánh xe và mặt đường dính bùn nhỏ, làm cho bánh xe không bám vào mặt đường được. Trường hợp này lực ma sát có lợi vì nhờ có nó mà xe mới đi chuyến được và không bị sa lầy.', 'Câu 13'),
                                                                                                   (74, 'MULTIPLE_CHOICE', 'Từ hình vẽ, hãy xác định chiều dài của khối hộp?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1 đầu của khối hộp ở vạch 2cm, đầu còn lại của khối hộp ở vạch 5cm Chiều dài của khối hộp là: 5 – 2 = 3cm.', 'Câu 14'),
                                                                                                   (75, 'MULTIPLE_CHOICE', 'Người ta sử các dụng thiết bị như trên hình 3.2 để đo khối lượng của 1cm3 nước bằng cách chia khối lượng của nước cho thể tích của nó đo bằng cm3. Hãy sắp xếp các bước theo đúng thứ tự thực hiện, bắt đầu là D.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Các bước theo đúng thứ tự là: D. Đặt ống đong rỗng lên cân -> F. Ghi lại khối lượng của ống đong rỗng -> C. Lấy ống đong rỗng ra khỏi cân -> A. Đổ 50cm3 nước vào ống đong -> H. Đặt ống đong chứa nước lên cân -> G. Ghi lại khối lượng của ống đong và nước -> E. Lấy khối lượng của ống đong chứa nước trừ đi khối lượng của ống đong rỗng -> B. Chia khối lượng của nước cho 50. Chuỗi đúng: D - F - C - A - H - G - E - B.', 'Câu 15'),
                                                                                                   (76, 'MULTIPLE_CHOICE', 'Trường hợp nào sau đây đều là chất?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Dãy gồm các chất là: nhôm, muối ăn, đường mía.', 'Câu 16'),
                                                                                                   (77, 'MULTIPLE_CHOICE', 'Vật liệu nào sau đây không thể tái chế?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vật liệu không thể tái chế là xi măng.', 'Câu 17'),
                                                                                                   (78, 'MULTIPLE_CHOICE', 'Chỉ ra đâu là tính chất hóa học của chất', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Cơm để lâu bị mốc có nghĩa là cơm đã bị biến đổi thành chất khác.', 'Câu 18'),
                                                                                                   (79, 'MULTIPLE_CHOICE', 'Để bảo vệ không khí trong lành chúng ta nên', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Để bảo vệ không khí trong lành chúng ta nên trồng cây xanh.', 'Câu 19'),
                                                                                                   (80, 'MULTIPLE_CHOICE', 'Điền từ thích hợp vào chỗ trống trong phát biểu sau: “Nguyên liệu là vật liệu…. chưa qua xử lí và cần được chuyển hóa để tạo ra sản phẩm”', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nguyên liệu là vật liệu thô chưa qua xử lí và cần được chuyển hóa để tạo ra sản phẩm.', 'Câu 20'),
                                                                                                   (81, 'MULTIPLE_CHOICE', 'Nước đựng trong cốc bay hơi càng nhanh khi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nước đựng trong cốc bay hơi càng nhanh khi nước trong cốc càng nóng, cốc rộng, cốc đặt chỗ gió to.', 'Câu 21'),
                                                                                                   (82, 'MULTIPLE_CHOICE', 'Để duy trì một sức khỏe tốt với chế độ ăn hợp lí ta nên làm gì?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Để duy trì một sức khỏe tốt với chế độ ăn hợp lí ta nên ăn đủ, đa dạng.', 'Câu 22'),
                                                                                                   (83, 'MULTIPLE_CHOICE', 'Cách sử dụng nhiên liệu hiệu quả, tiết kiệm là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Cách sử dụng nhiên liệu hiệu quả, tiết kiệm là đập than vừa nhỏ, chẻ nhỏ củi.', 'Câu 23'),
                                                                                                   (84, 'MULTIPLE_CHOICE', 'Nhóm thức ăn nào dưới đây là dạng lương thực?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nhóm thuộc loại lương thực là: gạo, khoai lang, lúa mì, ngô nếp.', 'Câu 24'),
                                                                                                   (85, 'MULTIPLE_CHOICE', 'Khi khai thác quặng sắt, ý nào sau đây là không đúng?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Khai thác bằng phương pháp thủ công (Đáp án C) là phát biểu không đúng khi khai thác quặng sắt.', 'Câu 25'),
                                                                                                   (86, 'MULTIPLE_CHOICE', 'Quan sát tế bào bên và cho biết mũi tên đang chỉ vào thành phần nào của tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Đây là tế bào động vật, mũi tên đang chỉ vào nhân tế bào.', 'Câu 26'),
                                                                                                   (87, 'MULTIPLE_CHOICE', 'Tế bào vi khuẩn có kích thước', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Tế bào vi khuẩn có kích thước rất nhỏ chỉ khoảng 0,5 - 10μm.', 'Câu 27'),
                                                                                                   (88, 'MULTIPLE_CHOICE', 'Cơ thể lớn lên nhờ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Cơ thể lớn lên nhờ tăng số lượng và kích thước tế bào (sự lớn lên và phân chia tế bào).', 'Câu 28'),
                                                                                                   (89, 'MULTIPLE_CHOICE', 'Có bao nhiêu phát biểu sau đây sai? (1) Cơ thể đơn bào có tổ chức đơn giản, cơ thể chỉ là một tế bào. (2) Vi khuẩn, nấm men, ... là cơ thể đơn bào. (3) Cơ thể đa bào có cấu tạo gồm nhiều hơn một tế bào. Mỗi loại tế bào thường thực hiện một chức năng sống riêng biệt nhưng phối hợp với nhau thực hiện các quá trình sống của cơ thể. (4) Trùng roi, cây bưởi, cây lim, con gà, con chó, ... là cơ thể đơn bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trong 4 phát biểu trên thì phát biểu (4) sai, cây bưởi, cây lim, con gà, chó là cơ thể đa bào. Vậy có 1 phát biểu sai.', 'Câu 29'),
                                                                                                   (90, 'MULTIPLE_CHOICE', 'Cho các nhận xét sau: (1) Tế bào thực vật và tế bào động vật đều có các bào quan. (2) Lục lạp là bào quan có ở tế bào động vật. (3) Tế bào động vật và tế bào thực vật đều có màng tế bào, tế bào chất và nhân. (4) Thành tế bào chỉ có ở tế bào động vật. (5) Lục lạp mang sắc tố quang hợp, có khả năng hấp thụ ánh sáng để tổng hợp nên chất hữu cơ. Các nhận xét đúng là:', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Xét các phát biểu: (1) đúng, (2) sai vì lục lạp chỉ có ở thực vật, (3) đúng, (4) sai vì thành tế bào có ở thực vật/nấm/vi khuẩn, (5) đúng. Các nhận xét đúng là (1), (3), (5).', 'Câu 30'),
                                                                                                   (91, 'MULTIPLE_CHOICE', 'Đâu là một cơ quan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Dạ dày là 1 cơ quan. Hệ tiêu hóa và hệ bài tiết là cấp độ hệ cơ quan.', 'Câu 31'),
                                                                                                   (92, 'MULTIPLE_CHOICE', 'Trình tự các bước làm tiêu bản quan sát sinh vật đơn bào là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trình tự các bước là: b) Khuấy đều nước -> a) Hút một giọt lên lam kính -> d) Dùng giấy thấm hút nước tràn -> c) Quan sát bằng kính hiển vi. Thứ tự: b-a-d-c.', 'Câu 32'),
                                                                                                   (93, 'MULTIPLE_CHOICE', 'Máu trong hệ mạch của hệ tuần hoàn là cấp độ tổ chức nào dưới đây?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Máu trong hệ mạch là mô liên kết (cấp độ Mô).', 'Câu 33'),
                                                                                                   (94, 'MULTIPLE_CHOICE', 'Cho một số sinh vật sau: vi khuẩn E. coli, trùng roi, nấm men, xạ khuẩn, rêu, lúa nước, mực ống, san hô. Các sinh vật được cấu tạo từ tế bào nhân sơ là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Vi khuẩn E.coli và xạ khuẩn là sinh vật nhân sơ thuộc giới Khởi sinh.', 'Câu 34'),
                                                                                                   (95, 'MULTIPLE_CHOICE', 'Ngoài sữa chua, chúng ta còn sử dụng các sản phẩm có ứng dụng hoạt động của vi khuẩn nào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Trong quá trình làm nước mắm thì vi khuẩn phân giải protein có trong cá.', 'Câu 35'),
                                                                                                   (96, 'MULTIPLE_CHOICE', 'Sinh vật thuộc giới nào sau đây có đặc điểm cấu tạo nhân tế bào khác hẳn với các giới còn lại?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Giới Khởi sinh gồm những tế bào nhân sơ còn 3 giới còn lại đều gồm các tế bào nhân thực.', 'Câu 36'),
                                                                                                   (97, 'MULTIPLE_CHOICE', 'Cấp bậc trên loài, dưới họ ở động vật được gọi là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Các bậc phân loại động vật theo thứ tự từ thấp đến cao: Loài → Giống (Chi) → Họ. Bậc trên loài dưới họ là Giống.', 'Câu 37'),
                                                                                                   (98, 'MULTIPLE_CHOICE', 'Người ta đã “lợi dụng” hoạt động của vi khuẩn lactic để tạo ra món ăn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Người ta đã “lợi dụng” hoạt động của vi khuẩn lactic để tạo ra sữa chua.', 'Câu 38'),
                                                                                                   (99, 'MULTIPLE_CHOICE', 'Các biểu hiện của người mắc COVID-19 là', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Các triệu chứng thường gặp của COVID-19 bao gồm cả sốt, ho và khó thở.', 'Câu 39'),
                                                                                                   (100, 'MULTIPLE_CHOICE', 'Nguyên sinh vật được chia thành', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nguyên sinh vật được chia thành 3 loại: Động vật nguyên sinh, thực vật nguyên sinh và nấm nhầy.', 'Câu 40');


-- 3. Ánh xạ Câu hỏi vào Exam số 2 (ExamQuestion)
INSERT INTO exam_question (id, point, question_order, created_date, updated_date, exam_id, question_id) VALUES
                                                                                                            (61, 1.0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 61),
                                                                                                            (62, 1.0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 62),
                                                                                                            (63, 1.0, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 63),
                                                                                                            (64, 1.0, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 64),
                                                                                                            (65, 1.0, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 65),
                                                                                                            (66, 1.0, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 66),
                                                                                                            (67, 1.0, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 67),
                                                                                                            (68, 1.0, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 68),
                                                                                                            (69, 1.0, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 69),
                                                                                                            (70, 1.0, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 70),
                                                                                                            (71, 1.0, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 71),
                                                                                                            (72, 1.0, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 72),
                                                                                                            (73, 1.0, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 73),
                                                                                                            (74, 1.0, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 74),
                                                                                                            (75, 1.0, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 75),
                                                                                                            (76, 1.0, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 76),
                                                                                                            (77, 1.0, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 77),
                                                                                                            (78, 1.0, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 78),
                                                                                                            (79, 1.0, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 79),
                                                                                                            (80, 1.0, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 80),
                                                                                                            (81, 1.0, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 81),
                                                                                                            (82, 1.0, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 82),
                                                                                                            (83, 1.0, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 83),
                                                                                                            (84, 1.0, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 84),
                                                                                                            (85, 1.0, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 85),
                                                                                                            (86, 1.0, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 86),
                                                                                                            (87, 1.0, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 87),
                                                                                                            (88, 1.0, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 88),
                                                                                                            (89, 1.0, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 89),
                                                                                                            (90, 1.0, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 90),
                                                                                                            (91, 1.0, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 91),
                                                                                                            (92, 1.0, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 92),
                                                                                                            (93, 1.0, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 93),
                                                                                                            (94, 1.0, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 94),
                                                                                                            (95, 1.0, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 95),
                                                                                                            (96, 1.0, 36, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 96),
                                                                                                            (97, 1.0, 37, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 97),
                                                                                                            (98, 1.0, 38, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 98),
                                                                                                            (99, 1.0, 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 99),
                                                                                                            (100, 1.0, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 100);


-- 4. Tạo 4 Tùy chọn đáp án (Answer) cho mỗi Câu hỏi (Tổng cộng 160 tùy chọn)
-- Căn cứ thuộc tính is_correct = TRUE dựa trên đáp án chính xác của Ban chuyên môn
INSERT INTO answer (id, type, content, created_date, updated_date, explanation, description, is_correct, question_id) VALUES
-- Câu 1 (Đúng: C)
(241, 'TEXT', 'bình chia độ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 61),
(242, 'TEXT', 'bình tràn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 61),
(243, 'TEXT', 'cân.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 61),
(244, 'TEXT', 'thước mét.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 61),
-- Câu 2 (Đúng: A)
(245, 'TEXT', 'Trọng lượng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 62),
(246, 'TEXT', 'Lực đẩy.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 62),
(247, 'TEXT', 'Lực kéo.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 62),
(248, 'TEXT', 'Lực đàn hồi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 62),
-- Câu 3 (Đúng: D)
(249, 'TEXT', 'Lực kế là dụng cụ để đo khối lượng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 63),
(250, 'TEXT', 'Lực kế là dụng cụ đo trọng lượng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 63),
(251, 'TEXT', 'Lực kế là dụng cụ để đo cả trọng lượng và khối lượng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 63),
(252, 'TEXT', 'Lực kế là dụng cụ để đo lực.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 63),
-- Câu 4 (Đúng: C)
(253, 'TEXT', 'Nam và Hòa cùng đẩy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 64),
(254, 'TEXT', 'Nam kéo và Hòa đẩy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 64),
(255, 'TEXT', 'Nam đẩy và Hòa kéo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 64),
(256, 'TEXT', 'Nam và Hòa cùng kéo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 64),
-- Câu 5 (Đúng: B)
(257, 'TEXT', 'Hoạt động a, b, c.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 65),
(258, 'TEXT', 'Hoạt động a, b.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 65),
(259, 'TEXT', 'Hoạt động a, b, d.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 65),
(260, 'TEXT', 'Hoạt động a, c.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 65),
-- Câu 6 (Đúng: A)
(261, 'TEXT', 'Đổ hóa chất vào cống thoát nước.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 66),
(262, 'TEXT', 'Đeo găng tay và kính bảo hộ khi làm thí nghiệm với hóa chất và lửa.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 66),
(263, 'TEXT', 'Thông báo với thầy cô giáo và các bạn khi gặp sự cố như đánh đổ hóa chất, làm vỡ ống nghiệm,...', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 66),
(264, 'TEXT', 'Rửa tay bằng nước sạch và xà phòng khi kết thúc buổi thực hành.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 66),
-- Câu 7 (Đúng: D)
(265, 'TEXT', 'trọng lực.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 67),
(266, 'TEXT', 'lực hấp dẫn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 67),
(267, 'TEXT', 'lực búng của tay.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 67),
(268, 'TEXT', 'lực ma sát.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 67),
-- Câu 8 (Đúng: D)
(269, 'TEXT', '37oC = 70,9oF; 50oF = 10oC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 68),
(270, 'TEXT', '37oC = 70,9oF; 50oF = 18oC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 68),
(271, 'TEXT', '37oC = 98,6oF; 50oF = 18oC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 68),
(272, 'TEXT', '37oC = 98,6oF; 50oF = 10oC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 68),
-- Câu 9 (Đúng: A)
(273, 'TEXT', '75 nhịp/phút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 69),
(274, 'TEXT', '80 nhịp/phút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 69),
(275, 'TEXT', '48 nhịp/phút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 69),
(276, 'TEXT', '2880 nhịp/phút.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 69),
-- Câu 10 (Đúng: B)
(277, 'TEXT', 'Lực của người đẩy thùng hàng có phương nằm ngang, chiều hướng từ trái sang phải, cường độ 30 N.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 70),
(278, 'TEXT', 'Lực của người đẩy thùng hàng có phương nằm ngang, chiều hướng từ trái sang phải, cường độ 20 N.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 70),
(279, 'TEXT', 'Lực của người đẩy thùng hàng có phương nằm ngang, chiều hướng từ phải sang trái, cường độ 30 N.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 70),
(280, 'TEXT', 'Lực của người đẩy thùng hàng có phương nằm ngang, chiều hướng từ phải sang trái, cường độ 20 N.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 70),
-- Câu 11 (Đúng: D)
(281, 'TEXT', 'Chỉ làm biến đổi chuyển động cọc tre.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 71),
(282, 'TEXT', 'Không làm biến dạng và cũng không làm biến đổi chuyến động của cọc tre.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 71),
(283, 'TEXT', 'Chỉ làm biến dạng cọc tre.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 71),
(284, 'TEXT', 'Vừa làm biến dạng cọc tre vừa làm biến đổi chuyển động của nó.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 71),
-- Câu 12 (Đúng: C)
(285, 'TEXT', '+ Lực tiếp xúc: hình b; hình c; hình d. + Lực không tiếp xúc: hình a; hình b.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 72),
(286, 'TEXT', '+ Lực tiếp xúc: hình b; hình d + Lực không tiếp xúc: hình a; hình c.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 72),
(287, 'TEXT', '+ Lực tiếp xúc: hình b; hình c; hình d + Lực không tiếp xúc: hình a.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 72),
(288, 'TEXT', '+ Lực tiếp xúc: hình a; hình b; hình c. + Lực không tiếp xúc: hình d.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 72),
-- Câu 13 (Đúng: C)
(289, 'TEXT', 'Ô tô đi trên bùn dễ bị sa lầy vì lực ma sát giữa bánh xe và mặt đường dính bùn nhỏ, làm cho bánh xe không bám vào mặt đường được. Trường hợp này lực ma sát có hại.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 73),
(290, 'TEXT', 'Ô tô đi trên bùn dễ bị sa lầy vì lực ma sát giữa bánh xe và mặt đường dính bùn lớn, làm cho bánh xe không bám vào mặt đường được. Trường hợp này lực ma sát có lợi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 73),
(291, 'TEXT', 'Ô tô đi trên bùn dễ bị sa lầy vì lực ma sát giữa bánh xe và mặt đường dính bùn nhỏ, làm cho bánh xe không bám vào mặt đường được. Trường hợp này lực ma sát có lợi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 73),
(292, 'TEXT', 'Ô tô đi trên bùn dễ bị sa lầy vì lực ma sát giữa bánh xe và mặt đường dính bùn lớn, làm cho bánh xe không bám vào mặt đường được. Trường hợp này lực ma sát có hại.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 73),
-- Câu 14 (Đúng: A)
(293, 'TEXT', '3cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 74),
(294, 'TEXT', '4cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 74),
(295, 'TEXT', '2cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 74),
(296, 'TEXT', '5cm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 74),
-- Câu 15 (Đúng: B)
(297, 'TEXT', 'D - C - A - F - H - G - E - B.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 75),
(298, 'TEXT', 'D - F - C - A - H - G - E - B.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 75),
(299, 'TEXT', 'D - F - C - A - B - G - E - H.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 75),
(300, 'TEXT', 'D - F - C - A - H - G - B - E.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 75),
-- Câu 16 (Đúng: C)
(301, 'TEXT', 'Đường mía, muối ăn, con dao.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 76),
(302, 'TEXT', 'Con dao, đôi đũa, cái thìa nhôm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 76),
(303, 'TEXT', 'Nhôm, muối ăn, đường mía.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 76),
(304, 'TEXT', 'Con dao, đôi đũa, muối ăn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 76),
-- Câu 17 (Đúng: D)
(305, 'TEXT', 'Thuỷ tinh.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 77),
(306, 'TEXT', 'Thép xây dựng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 77),
(307, 'TEXT', 'Nhựa.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 77),
(308, 'TEXT', 'Xi măng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 77),
-- Câu 18 (Đúng: D)
(309, 'TEXT', 'Đường tan vào nước.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 78),
(310, 'TEXT', 'Kem chảy lỏng khi để ngoài trời.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 78),
(311, 'TEXT', 'Tuyết tan khi thời tiết ấm dần.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 78),
(312, 'TEXT', 'Cơm để lâu bị mốc.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 78),
-- Câu 19 (Đúng: C)
(313, 'TEXT', 'chặt cây xây cầu cao tốc.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 79),
(314, 'TEXT', 'đổ chất thải chưa qua xử lí ra môi trường.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 79),
(315, 'TEXT', 'trồng cây xanh.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 79),
(316, 'TEXT', 'xây thêm nhiều khu công nghiệp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 79),
-- Câu 20 (Đúng: A)
(317, 'TEXT', 'Thô.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 80),
(318, 'TEXT', 'Tổng hợp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 80),
(319, 'TEXT', 'Bán tổng hợp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 80),
(320, 'TEXT', 'Nhân tạo.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 80),
-- Câu 21 (Đúng: C)
(321, 'TEXT', 'nước trong cốc càng nhiều, cốc rộng, cốc đặt chỗ nắng to.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 81),
(322, 'TEXT', 'nước trong cốc càng ít, cốc rộng, cốc được đậy lắp kín.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 81),
(323, 'TEXT', 'nước trong cốc càng nóng, cốc rộng, cốc đặt chỗ gió to.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 81),
(324, 'TEXT', 'nước trong cốc càng lạnh, cốc nhỏ, cốc đặt chỗ kín gió.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 81),
-- Câu 22 (Đúng: C)
(325, 'TEXT', 'Kiên trì chạy bộ.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 82),
(326, 'TEXT', 'Liên tục ăn các chất dinh dưỡng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 82),
(327, 'TEXT', 'Ăn đủ, đa dạng.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 82),
(328, 'TEXT', 'Tập trung vào việc học nhiều hơn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 82),
-- Câu 23 (Đúng: B)
(329, 'TEXT', 'điều chỉnh bếp gas nhỏ lửa nhất.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 83),
(330, 'TEXT', 'đập than vừa nhỏ, chẻ nhỏ củi.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 83),
(331, 'TEXT', 'dùng quạt thổi vào bếp củi khi đang cháy.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 83),
(332, 'TEXT', 'cho nhiều than, củi vào trong bếp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 83),
-- Câu 24 (Đúng: D)
(333, 'TEXT', 'Gạo, rau muống, khoai lang, thịt lợn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 84),
(334, 'TEXT', 'Khoai tây, lúa mì, quả bí ngô, cà rốt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 84),
(335, 'TEXT', 'Thịt bò, trứng gà, cá trôi, cải bắp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 84),
(336, 'TEXT', 'Gạo, khoai lang, lúa mì, ngô nếp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 84),
-- Câu 25 (Đúng: C)
(337, 'TEXT', 'Khai thác tiết kiệm vì nguồn quặng có hạn.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 85),
(338, 'TEXT', 'Tránh làm ô nhiễm môi trường.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 85),
(339, 'TEXT', 'Nên sử dụng các phương pháp khai thác thủ công.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 85),
(340, 'TEXT', 'Chế biến quảng thành sản phẩm có giá trị để nâng cao hiệu quả kinh tế.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 85),
-- Câu 26 (Đúng: C)
(341, 'TEXT', 'Màng tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 86),
(342, 'TEXT', 'Chất tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 86),
(343, 'TEXT', 'Nhân tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 86),
(344, 'TEXT', 'Vùng nhân.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 86),
-- Câu 27 (Đúng: B)
(345, 'TEXT', '5 – 10 mm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 87),
(346, 'TEXT', '0,5 - 10μm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 87),
(347, 'TEXT', '10 - 100μm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 87),
(348, 'TEXT', '50 - 100μm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 87),
-- Câu 28 (Đúng: C)
(349, 'TEXT', 'Sự sinh sản của các tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 88),
(350, 'TEXT', 'Sự lớn lên của các tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 88),
(351, 'TEXT', 'Sự lớn lên và phân chia của các tế bào', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 88),
(352, 'TEXT', 'Các tế bào chết đi không được thay thế bằng các tế bào mới.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 88),
-- Câu 29 (Đúng: C)
(353, 'TEXT', '3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 89),
(354, 'TEXT', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 89),
(355, 'TEXT', '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 89),
(356, 'TEXT', '4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 89),
-- Câu 30 (Đúng: A)
(357, 'TEXT', '(1), (3), (5)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 90),
(358, 'TEXT', '(1), (2), (3).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 90),
(359, 'TEXT', '(2), (4), (5)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 90),
(360, 'TEXT', '(3), (4), (5).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 90),
-- Câu 31 (Đúng: C)
(361, 'TEXT', 'Hệ tiêu hóa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 91),
(362, 'TEXT', 'Tim và mạch máu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 91),
(363, 'TEXT', 'Dạ dày', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 91),
(364, 'TEXT', 'Hệ bài tiết', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 91),
-- Câu 32 (Đúng: B)
(365, 'TEXT', 'a-c-b-d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 92),
(366, 'TEXT', 'b-a-d-c', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 92),
(367, 'TEXT', 'c-d-a-b', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 92),
(368, 'TEXT', 'c-a-b-d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 92),
-- Câu 33 (Đúng: B)
(369, 'TEXT', 'Tế bào.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 93),
(370, 'TEXT', 'Mô.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 93),
(371, 'TEXT', 'Cơ quan.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 93),
(372, 'TEXT', 'Hệ cơ quan.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 93),
-- Câu 34 (Đúng: D)
(373, 'TEXT', 'Trùng roi, xạ khuẩn, mực ống', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 94),
(374, 'TEXT', 'Xan hô, xạ khuẩn, nấm men', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 94),
(375, 'TEXT', 'Nấm men, lúa nước, trùng roi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 94),
(376, 'TEXT', 'Vi khuẩn E. coli, xạ khuẩn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 94),
-- Câu 35 (Đúng: A)
(377, 'TEXT', 'Nước mắm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 95),
(378, 'TEXT', 'Kem đánh răng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 95),
(379, 'TEXT', 'Muối Iốt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 95),
(380, 'TEXT', 'Dầu ăn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 95),
-- Câu 36 (Đúng: D)
(381, 'TEXT', 'Giới Động vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 96),
(382, 'TEXT', 'Giới Nấm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 96),
(383, 'TEXT', 'Giới Thực vật.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 96),
(384, 'TEXT', 'Giới Khởi sinh.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 96),
-- Câu 37 (Đúng: C)
(385, 'TEXT', 'Bộ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 97),
(386, 'TEXT', 'Chi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 97),
(387, 'TEXT', 'Giống', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 97),
(388, 'TEXT', 'Ngành', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 97),
-- Câu 38 (Đúng: D)
(389, 'TEXT', 'bánh gai', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 98),
(390, 'TEXT', 'bánh mì', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 98),
(391, 'TEXT', 'giò lụa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 98),
(392, 'TEXT', 'sữa chua', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 98),
-- Câu 39 (Đúng: D)
(393, 'TEXT', 'Sốt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 99),
(394, 'TEXT', 'Ho', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 99),
(395, 'TEXT', 'Khó thở', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 99),
(396, 'TEXT', 'Cả 3 triệu chứng trên', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 99),
-- Câu 40 (Đúng: D)
(397, 'TEXT', 'Động vật nguyên sinh và thực vật nguyên sinh', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 100),
(398, 'TEXT', 'Động vật nguyên sinh và nấm nhầy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 100),
(399, 'TEXT', 'Thực vật nguyên sinh và nấm nhầy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE, 100),
(400, 'TEXT', 'Động vật nguyên sinh, thực vật nguyên sinh và nấm nhầy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, TRUE, 100);


-- 5. Tạo liên kết Hình ảnh cho Câu hỏi (QuestionImage) theo URL từ file cung cấp
INSERT INTO question_image (id, name, display_order, created_date, url, question_id) VALUES
                                                                                         (31, 'q2_img_5.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/21.png', 65),
                                                                                         (32, 'q2_img_10.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/22.png', 70),
                                                                                         (33, 'q2_img_12.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/23_1.png', 72),
                                                                                         (34, 'q2_img_14.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/24.png', 74),
                                                                                         (35, 'q2_img_15.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/25.png', 75),
                                                                                         (36, 'q2_img_16.png', 1, CURRENT_TIMESTAMP, 'https://img.loigiaihay.com/picture/2022/1122/26.png', 86);


-- 6. Cập nhật lại Database Sequences để đồng bộ dữ liệu auto-increment
SELECT setval('exam_id_seq', (SELECT MAX(id) FROM exam));
SELECT setval('question_id_seq', (SELECT MAX(id) FROM question));
SELECT setval('exam_question_id_seq', (SELECT MAX(id) FROM exam_question));
SELECT setval('answer_id_seq', (SELECT MAX(id) FROM answer));
SELECT setval('question_image_id_seq', (SELECT MAX(id) FROM question_image));