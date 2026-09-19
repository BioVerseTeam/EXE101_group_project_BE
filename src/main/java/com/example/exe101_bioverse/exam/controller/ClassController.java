package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(@RequestBody ClassRequest request) {
        ApiResponse<ClassResponse> response = new ApiResponse<>();
        try {
            ClassResponse classResponse = classService.createClass(request);
            response.setPayload(classResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> updateClass(@PathVariable Long id, @RequestBody ClassRequest request) {
        ApiResponse<ClassResponse> response = new ApiResponse<>();
        try {
            ClassResponse classResponse = classService.updateClass(id, request);
            response.setPayload(classResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassById(@PathVariable Long id) {
        ApiResponse<ClassResponse> response = new ApiResponse<>();
        try {
            ClassResponse classResponse = classService.getClassById(id);
            response.setPayload(classResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassByGrade(@PathVariable Integer grade) {
        ApiResponse<ClassResponse> response = new ApiResponse<>();
        try {
            ClassResponse classResponse = classService.getClassByGrade(grade);
            response.setPayload(classResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getAllClasses() {
        ApiResponse<List<ClassResponse>> response = new ApiResponse<>();
        try {
            List<ClassResponse> classResponses = classService.getAllClasses();
            response.setPayload(classResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long id) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            classService.deleteClass(id);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
