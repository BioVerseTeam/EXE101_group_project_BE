package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.*;
import com.example.exe101_bioverse.exam.dto.response.*;
import com.example.exe101_bioverse.exam.entity.Exam;

import java.util.List;

public interface ExamService {
    <T> T saveExam(ExamRequest examRequest, Class<T> returnType);
    List<ExamResponse> getAllExams();
    Exam internalGetById(Long id);
    List<ExamResponse> getExamsBySubjectName(String subjectName);
    List<ExamResponse> getExamsByType(String type);
    List<ExamResponse> getExamsByName(String name);
    List<ExamResponse> getExamsByCode(String code);

    // New methods from EXAM_MANAGEMENT_API_SPECIFICATION
    PageResponse<ExamCatalogResponse> getExamCatalog(int page, int size, String search, Long subjectId, Integer grade, String sort);
    ExamResponse getExamById(Long id);
    ExamResponse updateExam(Long id, ExamUpdateRequest request);
    void deleteExam(Long id);
    ExamDuplicateResponse duplicateExam(Long id, ExamDuplicateRequest request);
    ExamBuilderResponse getExamBuilder(Long id);
    ExamReorderResponse reorderQuestions(Long id, ExamQuestionsReorderRequest request);
    CompositeQuestionResponse addCompositeQuestion(Long id, CompositeQuestionRequest request);
    void removeQuestionFromExam(Long examId, Long questionId);
    List<ExamQuestionResponse> pickQuestionsFromBank(Long examId, PickFromBankRequest request);
}
