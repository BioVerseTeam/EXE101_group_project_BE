package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse>> saveAnswer(@RequestBody AnswerRequest answerRequest) {
        AnswerResponse answerResponse = answerService.saveAnswer(answerRequest);
        return ResponseEntity.ok(ApiResponse.success(answerResponse, "Lưu câu trả lời thành công"));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<AnswerResponse>>> getAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        List<AnswerResponse> answerResponses = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(ApiResponse.success(answerResponses));
    }
}
