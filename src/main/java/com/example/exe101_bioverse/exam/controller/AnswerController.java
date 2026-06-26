package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse>> saveAnswer(@RequestBody AnswerRequest answerRequest) {
        ApiResponse<AnswerResponse> response = new ApiResponse<>();
        try {
            AnswerResponse answerResponse = answerService.saveAnswer(answerRequest);
            response.setPayload(answerResponse);
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
    public ResponseEntity<ApiResponse<List<AnswerResponse>>> getAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        ApiResponse<List<AnswerResponse>> response = new ApiResponse<>();
        try {
            List<AnswerResponse> answerResponses = answerService.getAnswersByQuestionId(questionId);
            response.setPayload(answerResponses);
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
