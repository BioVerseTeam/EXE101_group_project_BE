package com.example.exe101_bioverse.ai.guardrail;

public record GuardrailDecision(
        GuardrailAction action,
        String reason,
        String message
) {
}
