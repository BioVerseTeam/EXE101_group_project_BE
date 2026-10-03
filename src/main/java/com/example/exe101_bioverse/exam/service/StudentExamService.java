package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.StudentExamSubmitRequest;
import com.example.exe101_bioverse.exam.dto.response.StudentExamAttemptDetailResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamHistoryResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamPaperResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamSubmitResponse;

public interface StudentExamService {

    StudentExamPaperResponse getAntiCheatExamPaper(Long examId);

    StudentExamSubmitResponse submitExam(Long examId, Long userId, StudentExamSubmitRequest request);

    StudentExamAttemptDetailResponse getExamAttemptDetail(Long attemptId, Long userId);

    StudentExamHistoryResponse getStudentHistory(Long userId);
}
