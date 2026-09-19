package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;
import com.example.exe101_bioverse.exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@RequestBody SubjectRequest request) {
        ApiResponse<SubjectResponse> response = new ApiResponse<>();
        try {
            SubjectResponse subjectResponse = subjectService.createSubject(request);
            response.setPayload(subjectResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable Long id, @RequestBody SubjectRequest request) {
        ApiResponse<SubjectResponse> response = new ApiResponse<>();
        try {
            SubjectResponse subjectResponse = subjectService.updateSubject(id, request);
            response.setPayload(subjectResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
        ApiResponse<SubjectResponse> response = new ApiResponse<>();
        try {
            SubjectResponse subjectResponse = subjectService.getSubjectById(id);
            response.setPayload(subjectResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjectsBySemesterId(@PathVariable Long semesterId) {
        ApiResponse<List<SubjectResponse>> response = new ApiResponse<>();
        try {
            List<SubjectResponse> subjectResponses = subjectService.getSubjectsBySemesterId(semesterId);
            response.setPayload(subjectResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        ApiResponse<List<SubjectResponse>> response = new ApiResponse<>();
        try {
            List<SubjectResponse> subjectResponses = subjectService.getAllSubjects();
            response.setPayload(subjectResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            subjectService.deleteSubject(id);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
