package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.exam.service.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-questions")
public class ExamQuestionController {

    @Autowired
    private ExamQuestionService examQuestionService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamQuestionResponse>> saveExamQuestion(@RequestBody ExamQuestionRequest examQuestionRequest) {
        ExamQuestionResponse examQuestionResponse = examQuestionService.saveExamQuestion(examQuestionRequest);
        return ResponseEntity.ok(ApiResponse.success(examQuestionResponse, "Lưu câu hỏi vào đề thi thành công"));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamId(@PathVariable("examId") Long examId) {
        List<ExamQuestionResponse> examQuestionResponses = examQuestionService.getExamQuestionsByExamId(examId);
        return ResponseEntity.ok(ApiResponse.success(examQuestionResponses));
    }

    @GetMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamIdAndQuestionId(
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId) {
        List<ExamQuestionResponse> examQuestionResponses = examQuestionService.getExamQuestionsByExamIdAndQuestionId(examId, questionId);
        return ResponseEntity.ok(ApiResponse.success(examQuestionResponses));
    }
}
