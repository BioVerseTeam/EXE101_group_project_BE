package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public ResponseEntity<ApiResponse<SemesterResponse>> createSemester(@RequestBody SemesterRequest request) {
        SemesterResponse semesterResponse = semesterService.createSemester(request);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse, "Tạo học kỳ thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> updateSemester(@PathVariable Long id, @RequestBody SemesterRequest request) {
        SemesterResponse semesterResponse = semesterService.updateSemester(id, request);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse, "Cập nhật học kỳ thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> getSemesterById(@PathVariable Long id) {
        SemesterResponse semesterResponse = semesterService.getSemesterById(id);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getSemestersByClassId(@PathVariable Long classId) {
        List<SemesterResponse> semesterResponses = semesterService.getSemestersByClassId(classId);
        return ResponseEntity.ok(ApiResponse.success(semesterResponses));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getAllSemesters() {
        List<SemesterResponse> semesterResponses = semesterService.getAllSemesters();
        return ResponseEntity.ok(ApiResponse.success(semesterResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa học kỳ thành công"));
    }
}
