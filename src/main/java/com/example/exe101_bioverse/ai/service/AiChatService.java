package com.example.exe101_bioverse.ai.service;

import com.example.exe101_bioverse.ai.dto.AiChatRequest;
import com.example.exe101_bioverse.ai.dto.AiChatResponse;

public interface AiChatService {
    AiChatResponse chat(AiChatRequest request);
}

