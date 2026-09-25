package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.entity.Answer;
import com.example.exe101_bioverse.exam.entity.Question;

import java.util.List;

public interface AnswerService {
    AnswerResponse saveAnswer(AnswerRequest answerRequest);
    List<Answer> internalSaveAnswers(Question question, List<AnswerRequest> answerRequestList);
    List<AnswerResponse> getAnswersByQuestionId(Long questionId);
    AnswerResponse addAnswerToQuestion(Long questionId, AnswerRequest request);
    AnswerResponse updateAnswer(Long id, AnswerRequest request);
    void deleteAnswer(Long id);
}
