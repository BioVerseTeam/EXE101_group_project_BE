package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.service.SemesterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@Validated
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public ResponseEntity<ApiResponse<SemesterResponse>> createSemester(@Valid @RequestBody SemesterRequest request) {
        SemesterResponse semesterResponse = semesterService.createSemester(request);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse, "Tạo học kỳ thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> updateSemester(
            @PathVariable @Positive(message = "ID học kỳ phải lớn hơn 0") Long id,
            @Valid @RequestBody SemesterRequest request) {
        SemesterResponse semesterResponse = semesterService.updateSemester(id, request);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse, "Cập nhật học kỳ thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> getSemesterById(
            @PathVariable @Positive(message = "ID học kỳ phải lớn hơn 0") Long id) {
        SemesterResponse semesterResponse = semesterService.getSemesterById(id);
        return ResponseEntity.ok(ApiResponse.success(semesterResponse));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getSemestersByClassId(
            @PathVariable @Positive(message = "ID khối lớp phải lớn hơn 0") Long classId) {
        List<SemesterResponse> semesterResponses = semesterService.getSemestersByClassId(classId);
        return ResponseEntity.ok(ApiResponse.success(semesterResponses));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getAllSemesters() {
        List<SemesterResponse> semesterResponses = semesterService.getAllSemesters();
        return ResponseEntity.ok(ApiResponse.success(semesterResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSemester(
            @PathVariable @Positive(message = "ID học kỳ phải lớn hơn 0") Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa học kỳ thành công"));
    }
}
