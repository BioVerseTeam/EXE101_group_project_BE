package com.example.exe101_bioverse.ai.service;

import com.example.exe101_bioverse.ai.dto.AiChatRequest;
import com.example.exe101_bioverse.ai.dto.AiChatResponse;
import com.example.exe101_bioverse.ai.dto.ChatMessageResponse;
import com.example.exe101_bioverse.ai.dto.ConversationSummaryResponse;
import com.example.exe101_bioverse.ai.dto.PagedResponse;
import com.example.exe101_bioverse.auth.security.UserPrincipal;

import java.util.List;

public interface AiChatService {
    AiChatResponse chat(AiChatRequest request, UserPrincipal principal);

    PagedResponse<ConversationSummaryResponse> getUserConversations(Long userId, int limit, String cursor);

    List<ChatMessageResponse> getConversationMessages(Long userId, String conversationId, int limit);

    void deleteConversation(Long userId, String conversationId);
}
