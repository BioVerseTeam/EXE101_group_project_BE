package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question-images")
public class QuestionImageController {

    @Autowired
    private QuestionImageService questionImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionImageResponse>> saveQuestionImage(@RequestBody QuestionImageRequest request) {
        ApiResponse<QuestionImageResponse> response = new ApiResponse<>();
        try {
            QuestionImageResponse questionImageResponse = questionImageService.saveQuestionImage(request);
            response.setPayload(questionImageResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<QuestionImageResponse>>> getQuestionImagesByQuestionId(@PathVariable("questionId") Long questionId) {
        ApiResponse<List<QuestionImageResponse>> response = new ApiResponse<>();
        try {
            List<QuestionImageResponse> questionImageResponses = questionImageService.getQuestionImagesByQuestionId(questionId);
            response.setPayload(questionImageResponses);
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
