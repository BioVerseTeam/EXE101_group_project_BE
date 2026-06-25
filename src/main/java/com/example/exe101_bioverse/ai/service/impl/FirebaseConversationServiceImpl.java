package com.example.exe101_bioverse.ai.service.impl;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.repository.AiConversationRepository;
import com.example.exe101_bioverse.ai.service.FirebaseConversationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseConversationServiceImpl implements FirebaseConversationService {

    private final AiConversationRepository conversationRepository;

    @Value("${bioverse.ai.max-history-messages}")
    private int maxHistoryMessages;

    public FirebaseConversationServiceImpl(AiConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public String createOrGetConversation(String conversationId, String studentId, String firstQuestion) {
        return conversationRepository.createOrGetConversation(conversationId, studentId, firstQuestion);
    }

    @Override
    public void saveMessage(String conversationId, String role, String content) {
        conversationRepository.saveMessage(conversationId, role, content);
    }

    @Override
    public List<ChatMessage> getRecentMessages(String conversationId) {
        return conversationRepository.getRecentMessages(conversationId, maxHistoryMessages);
    }
}
