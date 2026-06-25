package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.entity.Exam;

import java.util.List;

public interface ExamService {
    <T> T saveExam(ExamRequest examRequest, Class<T> returnType);
    List<ExamResponse> getAllExams();
    Exam internalGetById(Long id);
    List<ExamResponse> getExamsBySubjectName (String subjectName);
    List<ExamResponse> getExamsByType (String type);
    List<ExamResponse> getExamsByName(String name);
    List<ExamResponse> getExamsByCode(String code);
}
