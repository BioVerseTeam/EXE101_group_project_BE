package com.example.exe101_bioverse.model.service;

import com.example.exe101_bioverse.model.dto.request.CreateReactionRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateReactionRequest;
import com.example.exe101_bioverse.model.dto.response.ReactionResponse;

import java.util.List;

public interface ReactionEquationService {

    List<ReactionResponse> listPublic();

    List<ReactionResponse> listAdmin();

    ReactionResponse getPublicByCode(String code);

    ReactionResponse create(CreateReactionRequest request);

    ReactionResponse update(Long id, UpdateReactionRequest request);

    void delete(Long id);
}
