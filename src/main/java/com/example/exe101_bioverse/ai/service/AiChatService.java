package com.example.exe101_bioverse.ai.service;

import com.example.exe101_bioverse.ai.client.OpenRouterClient;
import com.example.exe101_bioverse.ai.dto.AiChatRequest;
import com.example.exe101_bioverse.ai.dto.AiChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiChatService {

    private final FirebaseConversationService conversationService;
    private final PromptBuilder promptBuilder;
    private final OpenRouterClient openRouterClient;

    public AiChatService(FirebaseConversationService conversationService,
                         PromptBuilder promptBuilder,
                         OpenRouterClient openRouterClient) {
        this.conversationService = conversationService;
        this.promptBuilder = promptBuilder;
        this.openRouterClient = openRouterClient;
    }

    /**
     * Processes a chat request, records dialogue in Firestore, calls AI model, and stores response.
     */
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
            List<Map<String, String>> recentMessages = conversationService.getRecentMessages(conversationId);

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
