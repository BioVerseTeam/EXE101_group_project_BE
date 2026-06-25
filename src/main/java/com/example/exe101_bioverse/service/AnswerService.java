package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.AnswerRequest;
import com.example.exe101_bioverse.dto.response.AnswerResponse;
import com.example.exe101_bioverse.entity.Answer;
import com.example.exe101_bioverse.entity.Question;

import java.util.List;

public interface AnswerService {
    AnswerResponse saveAnswer(AnswerRequest answerRequest);
    List<Answer> internalSaveAnswers(Question question,List<AnswerRequest> answerRequestList);
    List<AnswerResponse> getAnswersByQuestionId(Long questionId);
}
