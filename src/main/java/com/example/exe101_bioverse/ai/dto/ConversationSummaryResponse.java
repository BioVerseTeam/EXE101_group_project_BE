package com.example.exe101_bioverse.ai.dto;

import java.time.Instant;

public record ConversationSummaryResponse(
        String conversationId,
        String title,
        Instant createdAt,
        Instant updatedAt
) {
}
