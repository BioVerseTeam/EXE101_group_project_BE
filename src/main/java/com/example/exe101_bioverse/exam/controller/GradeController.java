package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.service.GradeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@Validated
public class GradeController {

    @Autowired
    private GradeService classService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(@Valid @RequestBody ClassRequest request) {
        ClassResponse classResponse = classService.createClass(request);
        return ResponseEntity.ok(ApiResponse.success(classResponse, "Tạo khối lớp thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> updateClass(
            @PathVariable @Positive(message = "ID khối lớp phải lớn hơn 0") Long id,
            @Valid @RequestBody ClassRequest request) {
        ClassResponse classResponse = classService.updateClass(id, request);
        return ResponseEntity.ok(ApiResponse.success(classResponse, "Cập nhật khối lớp thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassById(
            @PathVariable @Positive(message = "ID khối lớp phải lớn hơn 0") Long id) {
        ClassResponse classResponse = classService.getClassById(id);
        return ResponseEntity.ok(ApiResponse.success(classResponse));
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassByGrade(
            @PathVariable @Min(value = 6, message = "Khối lớp phải từ lớp 6 đến lớp 9")
            @Max(value = 9, message = "Khối lớp phải từ lớp 6 đến lớp 9") Integer grade) {
        ClassResponse classResponse = classService.getClassByGrade(grade);
        return ResponseEntity.ok(ApiResponse.success(classResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getAllClasses() {
        List<ClassResponse> classResponses = classService.getAllClasses();
        return ResponseEntity.ok(ApiResponse.success(classResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(
            @PathVariable @Positive(message = "ID khối lớp phải lớn hơn 0") Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa khối lớp thành công"));
    }
}
