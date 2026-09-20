package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-images")
public class QuestionImageController {

    @Autowired
    private QuestionImageService questionImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionImageResponse>> saveQuestionImage(
            @RequestBody QuestionImageRequest request) {
        return ResponseEntity.ok(ApiResponse.success(questionImageService.saveQuestionImage(request)));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<QuestionImageResponse>>> getQuestionImagesByQuestionId(
            @PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(
                ApiResponse.success(questionImageService.getQuestionImagesByQuestionId(questionId)));
    }
}
