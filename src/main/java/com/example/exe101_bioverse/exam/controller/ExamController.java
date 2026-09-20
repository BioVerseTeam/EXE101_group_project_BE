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
        return ResponseEntity.ok(ApiResponse.success(examService.saveExam(examRequest, ExamResponse.class)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {
        return ResponseEntity.ok(ApiResponse.success(examService.getAllExams()));
    }

    @GetMapping("/subject/{subjectName}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsBySubjectName(
            @PathVariable String subjectName) {
        return ResponseEntity.ok(ApiResponse.success(examService.getExamsBySubjectName(subjectName)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByType(@PathVariable String type) {
        return ResponseEntity.ok(ApiResponse.success(examService.getExamsByType(type)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.success(examService.getExamsByName(name)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success(examService.getExamsByCode(code)));
    }
}
