package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.request.QuestionUpdateRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.enums.QuestionType;

import java.util.List;

public interface QuestionService {
    QuestionResponse getQuestionById(Long questionId);
    <T> T saveQuestion(QuestionRequest questionRequest, Class<T> returnType);
    Question internalSaveQuestion(QuestionRequest questionRequest);
    List<QuestionResponse> getQuestionsByExamId(Long examId);
    Question internalGetById(Long questionId);
    List<QuestionResponse> getQuestionByType(QuestionType questionType);
    PageResponse<QuestionResponse> getQuestionBank(String search, QuestionType type, int page, int size);
    QuestionResponse updateQuestion(Long id, QuestionUpdateRequest request);
    void deleteQuestion(Long id);
}
