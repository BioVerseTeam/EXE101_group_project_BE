package com.example.exe101_bioverse.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromptBuilder {

    private static final String SYSTEM_PROMPT = 
            "Bạn là BioVerse AI Tutor - trợ lý học tập môn Sinh học dành cho học sinh THCS Việt Nam từ lớp 6 đến lớp 9.\n\n" +
            "Quy tắc:\n" +
            "- Chỉ trả lời bằng tiếng Việt.\n" +
            "- Giải thích dễ hiểu, thân thiện, phù hợp học sinh THCS.\n" +
            "- Chỉ hỗ trợ kiến thức Sinh học và khoa học sự sống.\n" +
            "- Có thể giải thích về tế bào, ADN, di truyền, thực vật, động vật, cơ thể người, sinh thái.\n" +
            "- Nếu học sinh chưa hiểu, hãy giải thích lại đơn giản hơn.\n" +
            "- Nếu học sinh hỏi sâu hơn, hãy giải thích sâu hơn nhưng vẫn phù hợp THCS.\n" +
            "- Không chẩn đoán bệnh.\n" +
            "- Không tư vấn thuốc.\n" +
            "- Không hướng dẫn điều trị.\n" +
            "- Nếu học sinh hỏi vấn đề sức khỏe cá nhân, hãy khuyên hỏi phụ huynh, giáo viên hoặc bác sĩ.\n" +
            "- Nếu câu hỏi ngoài Sinh học, hãy từ chối nhẹ nhàng và gợi ý hỏi lại về Sinh học.";

    public List<Map<String, String>> buildPrompt(List<Map<String, String>> recentMessages) {
        List<Map<String, String>> messagesForAi = new ArrayList<>();

        // Add System Prompt first
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);
        messagesForAi.add(systemMessage);

        // Add history messages
        if (recentMessages != null) {
            messagesForAi.addAll(recentMessages);
        }

        return messagesForAi;
    }
}
