package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.request.QuestionUpdateRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.AnswerService;
import com.example.exe101_bioverse.exam.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Validated
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> saveQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        QuestionResponse questionResponse = questionService.saveQuestion(questionRequest, QuestionResponse.class);
        return ResponseEntity.ok(ApiResponse.success(questionResponse, "Lưu câu hỏi thành công"));
    }

    @GetMapping("/bank")
    public ResponseEntity<ApiResponse<PageResponse<QuestionResponse>>> getQuestionBank(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        PageResponse<QuestionResponse> questions = questionService.getQuestionBank(search, type, page, size);
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(
            @PathVariable("id") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId) {
        QuestionResponse questionResponse = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(ApiResponse.success(questionResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable("id") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId,
            @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionResponse updated = questionService.updateQuestion(questionId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật câu hỏi thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable("id") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa câu hỏi thành công"));
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<ApiResponse<AnswerResponse>> addAnswerToQuestion(
            @PathVariable("questionId") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId,
            @Valid @RequestBody AnswerRequest request) {
        AnswerResponse answerResponse = answerService.addAnswerToQuestion(questionId, request);
        return ResponseEntity.ok(ApiResponse.success(answerResponse, "Thêm đáp án cho câu hỏi thành công"));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByExamId(
            @PathVariable("examId") @Positive(message = "ID đề thi phải lớn hơn 0") Long examId) {
        List<QuestionResponse> questionResponses = questionService.getQuestionsByExamId(examId);
        return ResponseEntity.ok(ApiResponse.success(questionResponses));
    }

    @GetMapping("/type/{questionType}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionByType(
            @PathVariable("questionType") @NotNull(message = "Loại câu hỏi không được để trống") QuestionType questionType) {
        List<QuestionResponse> questionResponses = questionService.getQuestionByType(questionType);
        return ResponseEntity.ok(ApiResponse.success(questionResponses));
    }
}
