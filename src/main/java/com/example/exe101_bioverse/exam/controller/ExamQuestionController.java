package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.exam.service.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-questions")
public class ExamQuestionController {

    @Autowired
    private ExamQuestionService examQuestionService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamQuestionResponse>> saveExamQuestion(@RequestBody ExamQuestionRequest examQuestionRequest) {
        ApiResponse<ExamQuestionResponse> response = new ApiResponse<>();
        try {
            ExamQuestionResponse examQuestionResponse = examQuestionService.saveExamQuestion(examQuestionRequest);
            response.setPayload(examQuestionResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamId(@PathVariable("examId") Long examId) {
        ApiResponse<List<ExamQuestionResponse>> response = new ApiResponse<>();
        try {
            List<ExamQuestionResponse> examQuestionResponses = examQuestionService.getExamQuestionsByExamId(examId);
            response.setPayload(examQuestionResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> getExamQuestionsByExamIdAndQuestionId(@PathVariable("examId") Long examId, @PathVariable("questionId") Long questionId) {
        ApiResponse<List<ExamQuestionResponse>> response = new ApiResponse<>();
        try {
            List<ExamQuestionResponse> examQuestionResponses = examQuestionService.getExamQuestionsByExamIdAndQuestionId(examId, questionId);
            response.setPayload(examQuestionResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
