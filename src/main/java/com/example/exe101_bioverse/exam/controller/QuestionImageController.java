package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-images")
@Validated
public class QuestionImageController {

    @Autowired
    private QuestionImageService questionImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionImageResponse>> saveQuestionImage(@Valid @RequestBody QuestionImageRequest request) {
        QuestionImageResponse questionImageResponse = questionImageService.saveQuestionImage(request);
        return ResponseEntity.ok(ApiResponse.success(questionImageResponse, "Lưu ảnh câu hỏi thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestionImage(
            @PathVariable @Positive(message = "ID ảnh câu hỏi phải lớn hơn 0") Long id) {
        questionImageService.deleteQuestionImage(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa ảnh câu hỏi thành công"));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<QuestionImageResponse>>> getQuestionImagesByQuestionId(
            @PathVariable("questionId") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId) {
        List<QuestionImageResponse> questionImageResponses = questionImageService.getQuestionImagesByQuestionId(questionId);
        return ResponseEntity.ok(ApiResponse.success(questionImageResponses));
    }
}
