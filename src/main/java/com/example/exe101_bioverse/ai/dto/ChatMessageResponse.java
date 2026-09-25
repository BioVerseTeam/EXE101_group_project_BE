package com.example.exe101_bioverse.ai.dto;

import java.time.Instant;

public record ChatMessageResponse(
        String role,
        String content,
        Instant createdAt
) {
}
