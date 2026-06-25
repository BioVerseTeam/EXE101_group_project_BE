package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.dto.request.QuestionRequest;
import com.example.exe101_bioverse.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.entity.Exam;
import com.example.exe101_bioverse.entity.ExamQuestion;
import com.example.exe101_bioverse.entity.Question;

import java.util.List;

public interface ExamQuestionService {
    ExamQuestionResponse saveExamQuestion(ExamQuestionRequest examQuestionRequest);
    List<ExamQuestion> internalSaveExamQuestion(Exam exam,List<QuestionRequest> questionRequest);
    List<ExamQuestionResponse> getExamQuestionsByExamId(Long examId);
}
