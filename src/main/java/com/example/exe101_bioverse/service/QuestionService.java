package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.QuestionRequest;
import com.example.exe101_bioverse.dto.response.QuestionResponse;
import com.example.exe101_bioverse.entity.Exam;
import com.example.exe101_bioverse.entity.ExamQuestion;
import com.example.exe101_bioverse.entity.Question;
import com.example.exe101_bioverse.enums.QuestionType;

import java.util.List;

public interface QuestionService {
    QuestionResponse getQuestionById(Long questionId);
    <T> T saveQuestion(QuestionRequest questionRequest, Class<T> returnType);
    Question internalSaveQuestion(ExamQuestion examQuestion, QuestionRequest questionRequest);
    List<QuestionResponse> getQuestionsByExamId(Long examId);
    Question internalGetById(Long questionId);
    List<QuestionResponse> getQuestionByType (QuestionType questionType);
}
