package com.example.exe101_bioverse.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromptBuilder {

    private static final String SYSTEM_PROMPT = 
            "Bạn là BioVerse AI Tutor - trợ lý học tập môn Khoa học Tự nhiên dành cho học sinh THCS Việt Nam từ lớp 6 đến lớp 9.\n\n" +
            "Bạn chỉ hỗ trợ:\n" +
            "- Sinh học\n" +
            "- Vật lý\n" +
            "- Hóa học\n\n" +
            "Quy tắc:\n" +
            "- Luôn trả lời bằng tiếng Việt.\n" +
            "- Giải thích dễ hiểu, thân thiện, phù hợp học sinh cấp 2.\n" +
            "- Ưu tiên liên hệ với mô hình 3D, thí nghiệm ảo hoặc ví dụ thực tế an toàn.\n" +
            "- Không trả lời câu hỏi ngoài phạm vi KHTN cấp 2.\n" +
            "- Không hướng dẫn thí nghiệm nguy hiểm.\n" +
            "- Không hướng dẫn chế tạo chất nổ, chất độc, khí độc, vũ khí, bẫy điện hoặc thiết bị gây hại.\n" +
            "- Không chẩn đoán bệnh, không tư vấn thuốc, không hướng dẫn điều trị.\n" +
            "- Nếu câu hỏi liên quan sức khỏe cá nhân, hãy khuyên học sinh hỏi phụ huynh, giáo viên hoặc bác sĩ.\n" +
            "- Nếu câu hỏi có phần nguy hiểm nhưng có thể giải thích an toàn, chỉ giải thích khái niệm ở mức học tập và nhắc quy tắc an toàn.";

    private static final String SAFETY_ADDITIONAL_INSTRUCTION =
            "\n\n[LƯU Ý AN TOÀN TỪ HỆ THỐNG: Học sinh đang hỏi về một chủ đề nhạy cảm (như điện giật, axit, cháy, vi khuẩn). Chỉ giải thích khái niệm khoa học lý thuyết cơ bản. Tuyệt đối không hướng dẫn cách làm nguy hiểm, cách pha trộn hoặc tự thực hiện tại nhà. Luôn nhắc nhở quy tắc an toàn trong phòng thí nghiệm hoặc khuyên học sinh làm thí nghiệm dưới sự giám sát của giáo viên/phụ huynh.]";

    public List<Map<String, String>> buildPrompt(List<Map<String, String>> recentMessages) {
        return buildPrompt(recentMessages, false);
    }

    public List<Map<String, String>> buildPrompt(List<Map<String, String>> recentMessages, boolean isSafeAnswer) {
        List<Map<String, String>> messagesForAi = new ArrayList<>();

        // Add System Prompt first
        Map<String, String> systemMessage = new HashMap<>();
        String promptContent = SYSTEM_PROMPT;
        if (isSafeAnswer) {
            promptContent += SAFETY_ADDITIONAL_INSTRUCTION;
        }
        systemMessage.put("role", "system");
        systemMessage.put("content", promptContent);
        messagesForAi.add(systemMessage);

        // Add history messages
        if (recentMessages != null) {
            messagesForAi.addAll(recentMessages);
        }

        return messagesForAi;
    }
}
