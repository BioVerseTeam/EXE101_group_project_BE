package com.example.exe101_bioverse.ai.service.impl;

import com.example.exe101_bioverse.ai.client.OpenRouterClient;
import com.example.exe101_bioverse.ai.dto.AiChatRequest;
import com.example.exe101_bioverse.ai.dto.AiChatResponse;
import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.service.AiChatService;
import com.example.exe101_bioverse.ai.service.FirebaseConversationService;
import com.example.exe101_bioverse.ai.service.PromptBuilder;
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

    public AiChatServiceImpl(FirebaseConversationService conversationService,
                             PromptBuilder promptBuilder,
                             OpenRouterClient openRouterClient) {
        this.conversationService = conversationService;
        this.promptBuilder = promptBuilder;
        this.openRouterClient = openRouterClient;
    }

    @Override
    public AiChatResponse chat(AiChatRequest request) {
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

        // 2. Normalize student ID
        String studentId = request.studentId();
        if (studentId == null || studentId.trim().isEmpty()) {
            studentId = "anonymous-dev";
        }

        try {
            // 3. Create or retrieve existing conversation
            String conversationId = conversationService.createOrGetConversation(
                    request.conversationId(),
                    studentId,
                    request.question().trim()
            );

            // 4. Save User's query to Firestore
            conversationService.saveMessage(conversationId, "user", request.question().trim());

            // 5. Load recent history for dialogue memory context
            List<ChatMessage> recentChatMessages = conversationService.getRecentMessages(conversationId);

            // Map ChatMessage objects back to Map<String, String> format for PromptBuilder
            List<Map<String, String>> recentMessages = recentChatMessages.stream().map(msg -> {
                Map<String, String> m = new HashMap<>();
                m.put("role", msg.role());
                m.put("content", msg.content());
                return m;
            }).collect(Collectors.toList());

            // 6. Build query context payload containing System Prompt and history
            List<Map<String, String>> promptMessages = promptBuilder.buildPrompt(recentMessages);

            // 7. Invoke live AI completions from OpenRouter
            String answer = openRouterClient.ask(promptMessages);

            // 8. Save AI's response (only if the invoke succeeded without exception)
            conversationService.saveMessage(conversationId, "assistant", answer);

            // 9. Return updated conversationId and answer payload
            return new AiChatResponse(conversationId, answer);

        } catch (Exception e) {
            throw new RuntimeException("Error processing chat operation: " + e.getMessage(), e);
        }
    }
}
