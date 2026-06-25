package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.ExamRequest;
import com.example.exe101_bioverse.dto.response.ExamResponse;
import com.example.exe101_bioverse.entity.Exam;

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
