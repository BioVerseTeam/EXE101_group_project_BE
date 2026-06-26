package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> saveQuestion(@RequestBody QuestionRequest questionRequest) {
        ApiResponse<QuestionResponse> response = new ApiResponse<>();
        try {
            QuestionResponse questionResponse = questionService.saveQuestion(questionRequest, QuestionResponse.class);
            response.setPayload(questionResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(@PathVariable("id") Long questionId) {
        ApiResponse<QuestionResponse> response = new ApiResponse<>();
        try {
            QuestionResponse questionResponse = questionService.getQuestionById(questionId);
            response.setPayload(questionResponse);
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
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByExamId(@PathVariable("examId") Long examId) {
        ApiResponse<List<QuestionResponse>> response = new ApiResponse<>();
        try {
            List<QuestionResponse> questionResponses = questionService.getQuestionsByExamId(examId);
            response.setPayload(questionResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/type/{questionType}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionByType(@PathVariable("questionType") QuestionType questionType) {
        ApiResponse<List<QuestionResponse>> response = new ApiResponse<>();
        try {
            List<QuestionResponse> questionResponses = questionService.getQuestionByType(questionType);
            response.setPayload(questionResponses);
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
