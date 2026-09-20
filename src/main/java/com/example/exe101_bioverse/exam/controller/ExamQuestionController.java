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
    public ResponseEntity<ApiResponse<ExamQuestionResponse>> saveExamQuestion(
            @RequestBody ExamQuestionRequest examQuestionRequest) {
        return ResponseEntity.ok(ApiResponse.success(examQuestionService.saveExamQuestion(examQuestionRequest)));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamId(
            @PathVariable("examId") Long examId) {
        return ResponseEntity.ok(ApiResponse.success(examQuestionService.getExamQuestionsByExamId(examId)));
    }

    @GetMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamIdAndQuestionId(
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(ApiResponse.success(
                examQuestionService.getExamQuestionsByExamIdAndQuestionId(examId, questionId)));
    }
}
