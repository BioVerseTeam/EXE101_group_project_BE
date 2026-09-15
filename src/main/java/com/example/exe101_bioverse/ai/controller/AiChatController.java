package com.example.exe101_bioverse.ai.controller;

import com.example.exe101_bioverse.ai.dto.AiChatRequest;
import com.example.exe101_bioverse.ai.dto.AiChatResponse;
import com.example.exe101_bioverse.ai.service.AiChatService;
import com.example.exe101_bioverse.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * Public endpoint to receive user prompt, delegate to AI service and return answer.
     */
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AiChatResponse>> chat(@RequestBody AiChatRequest request) {
        AiChatResponse response = aiChatService.chat(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
