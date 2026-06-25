package com.example.exe101_bioverse.ai.dto;

public record AiChatRequest(
        String conversationId,
        String studentId,
        String question
) {
}
