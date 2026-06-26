package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResponse>> saveExam(@RequestBody ExamRequest examRequest) {
        ApiResponse<ExamResponse> response = new ApiResponse<>();
        try {
            ExamResponse examResponse = examService.saveExam(examRequest, ExamResponse.class);
            response.setPayload(examResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {
        ApiResponse<List<ExamResponse>> response = new ApiResponse<>();
        try {
            List<ExamResponse> examResponses = examService.getAllExams();
            response.setPayload(examResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/subject/{subjectName}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsBySubjectName(@PathVariable String subjectName) {
        ApiResponse<List<ExamResponse>> response = new ApiResponse<>();
        try {
            List<ExamResponse> examResponses = examService.getExamsBySubjectName(subjectName);
            response.setPayload(examResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByType(@PathVariable String type) {
        ApiResponse<List<ExamResponse>> response = new ApiResponse<>();
        try {
            List<ExamResponse> examResponses = examService.getExamsByType(type);
            response.setPayload(examResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByName(@PathVariable String name) {
        ApiResponse<List<ExamResponse>> response = new ApiResponse<>();
        try {
            List<ExamResponse> examResponses = examService.getExamsByName(name);
            response.setPayload(examResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, List<String>> error = Map.of("message", Collections.singletonList(e.getMessage()));
            response.setErrors(error);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByCode(@PathVariable String code) {
        ApiResponse<List<ExamResponse>> response = new ApiResponse<>();
        try {
            List<ExamResponse> examResponses = examService.getExamsByCode(code);
            response.setPayload(examResponses);
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
