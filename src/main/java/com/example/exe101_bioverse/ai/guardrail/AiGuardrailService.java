package com.example.exe101_bioverse.ai.guardrail;

public interface AiGuardrailService {
    GuardrailDecision checkQuestion(String question);
}
