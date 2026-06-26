package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answer-images")
public class AnswerImageController {

    @Autowired
    private AnswerImageService answerImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerImageResponse>> saveAnswerImage(@RequestBody AnswerImageRequest request) {
        ApiResponse<AnswerImageResponse> response = new ApiResponse<>();
        try {
            AnswerImageResponse answerImageResponse = answerImageService.saveAnswerImage(request);
            response.setPayload(answerImageResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/answer/{answerId}")
    public ResponseEntity<ApiResponse<List<AnswerImageResponse>>> getAnswerImagesByAnswerId(@PathVariable("answerId") Long answerId) {
        ApiResponse<List<AnswerImageResponse>> response = new ApiResponse<>();
        try {
            List<AnswerImageResponse> answerImageResponses = answerImageService.getAnswerImagesByAnswerId(answerId);
            response.setPayload(answerImageResponses);
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
