package com.example.exe101_bioverse.ai.controller;

import com.example.exe101_bioverse.ai.dto.*;
import com.example.exe101_bioverse.ai.service.AiChatService;
import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * Endpoint to receive user prompt, delegate to AI service and return answer.
     * Supports both guests and authenticated users.
     */
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AiChatResponse>> chat(
            @RequestBody AiChatRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        AiChatResponse response = aiChatService.chat(request, principal);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * List authenticated user's conversations with pagination.
     */
    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<PagedResponse<ConversationSummaryResponse>>> getUserConversations(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String cursor,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        PagedResponse<ConversationSummaryResponse> response = aiChatService.getUserConversations(
                principal != null ? principal.getId() : null,
                limit,
                cursor
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get historical messages for a specific conversation owned by the user.
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getConversationMessages(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "50") int limit,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<ChatMessageResponse> messages = aiChatService.getConversationMessages(
                principal != null ? principal.getId() : null,
                conversationId,
                limit
        );
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * Delete a conversation owned by the authenticated user.
     */
    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<ApiResponse<Void>> deleteConversation(
            @PathVariable String conversationId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        aiChatService.deleteConversation(
                principal != null ? principal.getId() : null,
                conversationId
        );
        return ResponseEntity.ok(ApiResponse.success(null, "Đã xóa cuộc trò chuyện thành công"));
    }
}
