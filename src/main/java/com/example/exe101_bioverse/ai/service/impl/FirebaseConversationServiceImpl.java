package com.example.exe101_bioverse.ai.service.impl;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.dto.ChatMessageResponse;
import com.example.exe101_bioverse.ai.dto.ConversationSummaryResponse;
import com.example.exe101_bioverse.ai.dto.PagedResponse;
import com.example.exe101_bioverse.ai.repository.AiConversationRepository;
import com.example.exe101_bioverse.ai.service.FirebaseConversationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FirebaseConversationServiceImpl implements FirebaseConversationService {

    private final AiConversationRepository conversationRepository;

    @Value("${bioverse.ai.max-history-messages:8}")
    private int maxHistoryMessages;

    public FirebaseConversationServiceImpl(AiConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public String createOrGetConversation(String conversationId, String userId, String studentId, String firstQuestion) {
        return conversationRepository.createOrGetConversation(conversationId, userId, studentId, firstQuestion);
    }

    @Override
    public void saveMessage(String conversationId, String role, String content) {
        conversationRepository.saveMessage(conversationId, role, content);
    }

    @Override
    public List<ChatMessage> getRecentMessages(String conversationId) {
        return conversationRepository.getRecentMessages(conversationId, maxHistoryMessages);
    }

    @Override
    public PagedResponse<ConversationSummaryResponse> getUserConversations(String userId, int limit, String cursor) {
        return conversationRepository.getUserConversations(userId, limit, cursor);
    }

    @Override
    public List<ChatMessageResponse> getConversationMessages(String conversationId, int limit) {
        return conversationRepository.getConversationMessages(conversationId, limit);
    }

    @Override
    public Map<String, Object> getConversationDocument(String conversationId) {
        return conversationRepository.getConversationDocument(conversationId);
    }

    @Override
    public void deleteConversation(String conversationId) {
        conversationRepository.deleteConversation(conversationId);
    }
}
