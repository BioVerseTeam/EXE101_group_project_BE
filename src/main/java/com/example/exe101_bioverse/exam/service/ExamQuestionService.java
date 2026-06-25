package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.exam.entity.Exam;
import com.example.exe101_bioverse.exam.entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {
    ExamQuestionResponse saveExamQuestion(ExamQuestionRequest examQuestionRequest);
    List<ExamQuestion> internalSaveExamQuestion(Exam exam,List<QuestionRequest> questionRequest);
    List<ExamQuestionResponse> getExamQuestionsByExamId(Long examId);
}
