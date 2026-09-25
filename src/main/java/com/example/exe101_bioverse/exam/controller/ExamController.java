package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResponse>> saveExam(@RequestBody ExamRequest examRequest) {
        ExamResponse examResponse = examService.saveExam(examRequest, ExamResponse.class);
        return ResponseEntity.ok(ApiResponse.success(examResponse, "Lưu đề thi thành công"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {
        List<ExamResponse> examResponses = examService.getAllExams();
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/subject/{subjectName}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsBySubjectName(@PathVariable String subjectName) {
        List<ExamResponse> examResponses = examService.getExamsBySubjectName(subjectName);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByType(@PathVariable String type) {
        List<ExamResponse> examResponses = examService.getExamsByType(type);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByName(@PathVariable String name) {
        List<ExamResponse> examResponses = examService.getExamsByName(name);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByCode(@PathVariable String code) {
        List<ExamResponse> examResponses = examService.getExamsByCode(code);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }
}
