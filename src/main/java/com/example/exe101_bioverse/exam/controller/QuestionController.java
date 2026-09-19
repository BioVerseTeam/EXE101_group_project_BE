package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> saveQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse questionResponse = questionService.saveQuestion(questionRequest, QuestionResponse.class);
        return ResponseEntity.ok(ApiResponse.success(questionResponse, "Lưu câu hỏi thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(@PathVariable("id") Long questionId) {
        QuestionResponse questionResponse = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(ApiResponse.success(questionResponse));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByExamId(@PathVariable("examId") Long examId) {
        List<QuestionResponse> questionResponses = questionService.getQuestionsByExamId(examId);
        return ResponseEntity.ok(ApiResponse.success(questionResponses));
    }

    @GetMapping("/type/{questionType}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionByType(@PathVariable("questionType") QuestionType questionType) {
        List<QuestionResponse> questionResponses = questionService.getQuestionByType(questionType);
        return ResponseEntity.ok(ApiResponse.success(questionResponses));
    }
}
