package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public ResponseEntity<ApiResponse<SemesterResponse>> createSemester(@RequestBody SemesterRequest request) {
        ApiResponse<SemesterResponse> response = new ApiResponse<>();
        try {
            SemesterResponse semesterResponse = semesterService.createSemester(request);
            response.setPayload(semesterResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> updateSemester(@PathVariable Long id, @RequestBody SemesterRequest request) {
        ApiResponse<SemesterResponse> response = new ApiResponse<>();
        try {
            SemesterResponse semesterResponse = semesterService.updateSemester(id, request);
            response.setPayload(semesterResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> getSemesterById(@PathVariable Long id) {
        ApiResponse<SemesterResponse> response = new ApiResponse<>();
        try {
            SemesterResponse semesterResponse = semesterService.getSemesterById(id);
            response.setPayload(semesterResponse);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getSemestersByClassId(@PathVariable Long classId) {
        ApiResponse<List<SemesterResponse>> response = new ApiResponse<>();
        try {
            List<SemesterResponse> semesterResponses = semesterService.getSemestersByClassId(classId);
            response.setPayload(semesterResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getAllSemesters() {
        ApiResponse<List<SemesterResponse>> response = new ApiResponse<>();
        try {
            List<SemesterResponse> semesterResponses = semesterService.getAllSemesters();
            response.setPayload(semesterResponses);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSemester(@PathVariable Long id) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            semesterService.deleteSemester(id);
            response.setStatus("success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("error");
            response.setErrors(Map.of("message", Collections.singletonList(e.getMessage())));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
