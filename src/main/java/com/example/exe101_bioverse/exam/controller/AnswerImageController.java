package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer-images")
public class AnswerImageController {

    @Autowired
    private AnswerImageService answerImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerImageResponse>> saveAnswerImage(
            @RequestBody AnswerImageRequest request) {
        return ResponseEntity.ok(ApiResponse.success(answerImageService.saveAnswerImage(request)));
    }

    @GetMapping("/answer/{answerId}")
    public ResponseEntity<ApiResponse<List<AnswerImageResponse>>> getAnswerImagesByAnswerId(
            @PathVariable("answerId") Long answerId) {
        return ResponseEntity.ok(ApiResponse.success(answerImageService.getAnswerImagesByAnswerId(answerId)));
    }
}
