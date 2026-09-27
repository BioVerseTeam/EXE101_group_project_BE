package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.service.AnswerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@Validated
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse>> saveAnswer(@Valid @RequestBody AnswerRequest answerRequest) {
        return ResponseEntity.ok(ApiResponse.success(answerService.saveAnswer(answerRequest), "Lưu đáp án thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnswerResponse>> updateAnswer(
            @PathVariable @Positive(message = "ID đáp án phải lớn hơn 0") Long id,
            @Valid @RequestBody AnswerRequest request) {
        AnswerResponse updated = answerService.updateAnswer(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật đáp án thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(
            @PathVariable @Positive(message = "ID đáp án phải lớn hơn 0") Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa đáp án thành công"));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<AnswerResponse>>> getAnswersByQuestionId(
            @PathVariable("questionId") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId) {
        return ResponseEntity.ok(ApiResponse.success(answerService.getAnswersByQuestionId(questionId)));
    }
}
