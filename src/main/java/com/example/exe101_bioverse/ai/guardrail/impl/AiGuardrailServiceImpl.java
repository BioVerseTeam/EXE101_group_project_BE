package com.example.exe101_bioverse.ai.guardrail.impl;

import com.example.exe101_bioverse.ai.guardrail.AiGuardrailService;
import com.example.exe101_bioverse.ai.guardrail.GuardrailAction;
import com.example.exe101_bioverse.ai.guardrail.GuardrailDecision;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AiGuardrailServiceImpl implements AiGuardrailService {

    // Regex ngoài phạm vi KHTN cấp 2
    private static final Pattern OUT_OF_SCOPE_PATTERN = Pattern.compile(
            "(?i)(code\\s+python|viết\\s+code|lập\\s+trình|văn\\s+học|bài\\s+văn|phân\\s+tích\\s+nhân\\s+vật|tác\\s+phẩm\\s+văn|mua\\s+coin|bitcoin|crypto|đầu\\s+tư\\s+coin|chứng\\s+khoán|tỏ\\s+tình|hẹn\\s+hò|yêu\\s+đương|chính\\s+trị|đảng\\s+phái|game\\s+hack|hack\\s+game)"
    );

    // Regex hóa học, vật lý nguy hiểm (chế tạo, gây hại)
    private static final Pattern DANGEROUS_PHYS_CHEM_PATTERN = Pattern.compile(
            "(?i)(làm.*bom|chế.*bom|thuốc\\s+nổ|chất\\s+nổ|khí\\s+độc|chất\\s+độc|chế.*vũ\\s+khí|chế.*súng|súng\\s+điện|bẫy\\s+điện|giật\\s+điện|làm.*điện\\s+giật|gây.*điện\\s+giật|laser\\s+gây\\s+hại|phá\\s+thiết\\s+bị|chế\\s+ma\\s+túy|đập\\s+đá|hút\\s+cần)"
    );

    // Regex y tế/sinh học nguy hiểm (chẩn đoán, khuyên thuốc cá nhân)
    private static final Pattern MEDICAL_HAZARD_PATTERN = Pattern.compile(
            "(?i)(uống\\s+thuốc\\s+gì|thuốc\\s+nào|liều\\s+thuốc|uống\\s+liều|tự\\s+điều\\s+trị|chẩn\\s+đoán|đau\\s+ngực.*uống|bị\\s+bệnh\\s+gì|chữa\\s+bệnh|kê\\s+đơn|nuôi\\s+cấy\\s+vi\\s+khuẩn\\s+nguy\\s+hiểm|lây\\s+bệnh)"
    );

    // Regex nội dung không phù hợp (người lớn, bạo lực, tự hại, gian lận)
    private static final Pattern INAPPROPRIATE_PATTERN = Pattern.compile(
            "(?i)(tự\\s+tử|tự\\s+sát|tự\\s+hại|cắt\\s+tay|khiêu\\s+dâm|đồi\\s+trụy|phim\\s+18|sex|bạo\\s+lực|đánh\\s+nhau|gian\\s+lận\\s+thi|quay\\s+cóp|tiết\\s+lộ\\s+thông\\s+tin|mật\\s+khẩu)"
    );

    // Regex nhận diện các câu hỏi nhạy cảm nhưng mang tính học tập lý thuyết (SAFE_ANSWER)
    private static final Pattern SAFE_ACADEMIC_PATTERN = Pattern.compile(
            "(?i)(điện\\s+giật\\s+nguy\\s+hiểm|axit|axit\\s+clohiđric|phản\\s+ứng\\s+cháy|vi\\s+khuẩn)"
    );

    @Override
    public GuardrailDecision checkQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return new GuardrailDecision(GuardrailAction.ALLOW, "Empty question", "");
        }

        String normalizedQuestion = question.trim();

        // 1. Kiểm tra các nhóm cần BLOCK trực tiếp
        if (DANGEROUS_PHYS_CHEM_PATTERN.matcher(normalizedQuestion).find()) {
            return new GuardrailDecision(
                    GuardrailAction.BLOCK,
                    "DANGEROUS_PHYS_CHEM",
                    "Vì lý do an toàn, mình không thể cung cấp thông tin hoặc hướng dẫn thực hiện các hoạt động nguy hiểm (như chế tạo chất nổ, vũ khí, chất độc hoặc bẫy điện). Bạn hãy đặt câu hỏi học tập an toàn khác nhé!"
            );
        }

        if (MEDICAL_HAZARD_PATTERN.matcher(normalizedQuestion).find()) {
            return new GuardrailDecision(
                    GuardrailAction.BLOCK,
                    "MEDICAL_HAZARD",
                    "Mình không thể đưa ra lời khuyên y tế, chẩn đoán bệnh hoặc tư vấn sử dụng thuốc. Nếu gặp vấn đề về sức khỏe, em hãy tham khảo ý kiến của cha mẹ, thầy cô hoặc bác sĩ nhé!"
            );
        }

        if (INAPPROPRIATE_PATTERN.matcher(normalizedQuestion).find()) {
            return new GuardrailDecision(
                    GuardrailAction.BLOCK,
                    "INAPPROPRIATE",
                    "Thông điệp của bạn chứa nội dung không phù hợp hoặc vi phạm quy tắc an toàn. Vui lòng đặt câu hỏi lịch sự và liên quan đến Khoa học Tự nhiên cấp 2."
            );
        }

        if (OUT_OF_SCOPE_PATTERN.matcher(normalizedQuestion).find()) {
            return new GuardrailDecision(
                    GuardrailAction.BLOCK,
                    "OUT_OF_SCOPE",
                    "Xin lỗi, mình là BioVerse AI Tutor và chỉ có thể hỗ trợ các câu hỏi thuộc môn Khoa học Tự nhiên cấp 2 (Sinh học, Vật lý, Hóa học). Bạn vui lòng đặt câu hỏi khác liên quan đến các môn học này nhé!"
            );
        }

        // 2. Kiểm tra nhóm SAFE_ANSWER (Nhạy cảm nhưng học tập)
        if (SAFE_ACADEMIC_PATTERN.matcher(normalizedQuestion).find()) {
            return new GuardrailDecision(
                    GuardrailAction.SAFE_ANSWER,
                    "SAFE_ACADEMIC_TOPIC",
                    "Câu hỏi nằm trong chủ đề nhạy cảm nhưng được phép giải thích lý thuyết ở mức độ học tập an toàn."
            );
        }

        // 3. Mặc định cho phép (ALLOW)
        return new GuardrailDecision(
                GuardrailAction.ALLOW,
                "ALLOW",
                ""
        );
    }
}
