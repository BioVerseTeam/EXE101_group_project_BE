package com.example.exe101_bioverse.ai.service.impl;

import com.example.exe101_bioverse.ai.client.OpenRouterClient;
import com.example.exe101_bioverse.ai.dto.*;
import com.example.exe101_bioverse.ai.guardrail.AiGuardrailService;
import com.example.exe101_bioverse.ai.guardrail.GuardrailAction;
import com.example.exe101_bioverse.ai.guardrail.GuardrailDecision;
import com.example.exe101_bioverse.ai.service.AiChatService;
import com.example.exe101_bioverse.ai.service.FirebaseConversationService;
import com.example.exe101_bioverse.ai.service.PromptBuilder;
import com.example.exe101_bioverse.auth.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiChatServiceImpl implements AiChatService {

    private final FirebaseConversationService conversationService;
    private final PromptBuilder promptBuilder;
    private final OpenRouterClient openRouterClient;
    private final AiGuardrailService aiGuardrailService;

    public AiChatServiceImpl(FirebaseConversationService conversationService,
                             PromptBuilder promptBuilder,
                             OpenRouterClient openRouterClient,
                             AiGuardrailService aiGuardrailService) {
        this.conversationService = conversationService;
        this.promptBuilder = promptBuilder;
        this.openRouterClient = openRouterClient;
        this.aiGuardrailService = aiGuardrailService;
    }

    @Override
    public AiChatResponse chat(AiChatRequest request, UserPrincipal principal) {
        // 1. Basic validation
        if (request == null) {
            throw new IllegalArgumentException("Request body must not be null.");
        }
        if (request.question() == null || request.question().trim().isEmpty()) {
            throw new IllegalArgumentException("Question must not be empty.");
        }
        if (request.question().length() > 1200) {
            throw new IllegalArgumentException("Question length exceeds the 1200 character limit.");
        }

        String userId = null;
        String studentId = null;

        if (principal != null && principal.getId() != null) {
            // Authenticated Mode: derive identity strictly from JWT UserPrincipal
            userId = String.valueOf(principal.getId());
            if (request.conversationId() != null && !request.conversationId().trim().isEmpty()) {
                Map<String, Object> doc = conversationService.getConversationDocument(request.conversationId().trim());
                if (doc != null) {
                    String docUserId = (String) doc.get("userId");
                    if (docUserId == null || !docUserId.equals(userId)) {
                        throw new AccessDeniedException("Bạn không có quyền truy cập cuộc trò chuyện này.");
                    }
                }
            }
        } else {
            // Guest Mode
            studentId = request.studentId();
            if (studentId == null || studentId.trim().isEmpty()) {
                studentId = "anonymous-dev";
            }
            if (request.conversationId() != null && !request.conversationId().trim().isEmpty()) {
                Map<String, Object> doc = conversationService.getConversationDocument(request.conversationId().trim());
                if (doc != null) {
                    String docUserId = (String) doc.get("userId");
                    if (docUserId != null && !docUserId.trim().isEmpty()) {
                        throw new AccessDeniedException("Khách không có quyền truy cập cuộc trò chuyện của người dùng.");
                    }
                    String docStudentId = (String) doc.get("studentId");
                    if (docStudentId != null && !docStudentId.equals(studentId)) {
                        throw new AccessDeniedException("Khách không có quyền truy cập cuộc trò chuyện này.");
                    }
                }
            }
        }

        try {
            // Create or retrieve conversation
            String conversationId = conversationService.createOrGetConversation(
                    request.conversationId(),
                    userId,
                    studentId,
                    request.question().trim()
            );

            // Save user message
            conversationService.saveMessage(conversationId, "user", request.question().trim());

            // Run guardrail safety check
            GuardrailDecision decision = aiGuardrailService.checkQuestion(request.question().trim());

            if (decision.action() == GuardrailAction.BLOCK) {
                String refusalMessage = decision.message();
                conversationService.saveMessage(conversationId, "assistant", refusalMessage);
                return new AiChatResponse(conversationId, refusalMessage);
            }

            // Load recent history (max 8 messages)
            List<ChatMessage> recentChatMessages = conversationService.getRecentMessages(conversationId);
            List<Map<String, String>> recentMessages = recentChatMessages.stream().map(msg -> {
                Map<String, String> m = new HashMap<>();
                m.put("role", msg.role());
                m.put("content", msg.content());
                return m;
            }).collect(Collectors.toList());

            // Build system prompt + history
            boolean isSafeAnswer = (decision.action() == GuardrailAction.SAFE_ANSWER);
            List<Map<String, String>> promptMessages = promptBuilder.buildPrompt(recentMessages, isSafeAnswer);

            // Invoke OpenRouter API
            String answer = openRouterClient.ask(promptMessages);

            // Save assistant response
            conversationService.saveMessage(conversationId, "assistant", answer);

            return new AiChatResponse(conversationId, answer);
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error processing chat operation: " + e.getMessage(), e);
        }
    }

    @Override
    public PagedResponse<ConversationSummaryResponse> getUserConversations(Long userId, int limit, String cursor) {
        if (userId == null) {
            throw new AccessDeniedException("Cần đăng nhập để xem lịch sử trò chuyện.");
        }
        return conversationService.getUserConversations(String.valueOf(userId), limit, cursor);
    }

    @Override
    public List<ChatMessageResponse> getConversationMessages(Long userId, String conversationId, int limit) {
        if (userId == null) {
            throw new AccessDeniedException("Cần đăng nhập để xem tin nhắn cuộc trò chuyện.");
        }
        Map<String, Object> doc = conversationService.getConversationDocument(conversationId);
        if (doc == null) {
            throw new IllegalArgumentException("Cuộc trò chuyện không tồn tại.");
        }
        String docUserId = (String) doc.get("userId");
        if (docUserId == null || !docUserId.equals(String.valueOf(userId))) {
            throw new AccessDeniedException("Bạn không có quyền xem cuộc trò chuyện này.");
        }
        return conversationService.getConversationMessages(conversationId, limit);
    }

    @Override
    public void deleteConversation(Long userId, String conversationId) {
        if (userId == null) {
            throw new AccessDeniedException("Cần đăng nhập để xóa cuộc trò chuyện.");
        }
        Map<String, Object> doc = conversationService.getConversationDocument(conversationId);
        if (doc == null) {
            return;
        }
        String docUserId = (String) doc.get("userId");
        if (docUserId == null || !docUserId.equals(String.valueOf(userId))) {
            throw new AccessDeniedException("Bạn không có quyền xóa cuộc trò chuyện này.");
        }
        conversationService.deleteConversation(conversationId);
    }
}
