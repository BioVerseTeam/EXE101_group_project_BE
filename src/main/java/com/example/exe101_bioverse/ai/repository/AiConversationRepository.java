package com.example.exe101_bioverse.ai.repository;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.dto.ChatMessageResponse;
import com.example.exe101_bioverse.ai.dto.ConversationSummaryResponse;
import com.example.exe101_bioverse.ai.dto.PagedResponse;

import java.util.List;
import java.util.Map;

public interface AiConversationRepository {
    String createOrGetConversation(String conversationId, String userId, String studentId, String firstQuestion);

    void saveMessage(String conversationId, String role, String content);

    List<ChatMessage> getRecentMessages(String conversationId, int limit);

    PagedResponse<ConversationSummaryResponse> getUserConversations(String userId, int limit, String cursor);

    List<ChatMessageResponse> getConversationMessages(String conversationId, int limit);

    Map<String, Object> getConversationDocument(String conversationId);

    void deleteConversation(String conversationId);
}
