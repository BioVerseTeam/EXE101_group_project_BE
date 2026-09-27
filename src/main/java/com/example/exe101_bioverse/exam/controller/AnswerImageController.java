package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer-images")
@Validated
public class AnswerImageController {

    @Autowired
    private AnswerImageService answerImageService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerImageResponse>> saveAnswerImage(@Valid @RequestBody AnswerImageRequest request) {
        AnswerImageResponse answerImageResponse = answerImageService.saveAnswerImage(request);
        return ResponseEntity.ok(ApiResponse.success(answerImageResponse, "Lưu ảnh đáp án thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswerImage(
            @PathVariable @Positive(message = "ID ảnh đáp án phải lớn hơn 0") Long id) {
        answerImageService.deleteAnswerImage(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa ảnh đáp án thành công"));
    }

    @GetMapping("/answer/{answerId}")
    public ResponseEntity<ApiResponse<List<AnswerImageResponse>>> getAnswerImagesByAnswerId(
            @PathVariable("answerId") @Positive(message = "ID đáp án phải lớn hơn 0") Long answerId) {
        List<AnswerImageResponse> answerImageResponses = answerImageService.getAnswerImagesByAnswerId(answerId);
        return ResponseEntity.ok(ApiResponse.success(answerImageResponses));
    }
}
