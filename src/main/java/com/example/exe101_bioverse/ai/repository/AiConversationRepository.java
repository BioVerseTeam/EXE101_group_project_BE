package com.example.exe101_bioverse.ai.repository;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import java.util.List;

public interface AiConversationRepository {
    String createOrGetConversation(String conversationId, String studentId, String firstQuestion);
    void saveMessage(String conversationId, String role, String content);
    List<ChatMessage> getRecentMessages(String conversationId, int limit);
}
