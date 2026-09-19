package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(@RequestBody ClassRequest request) {
        ClassResponse classResponse = classService.createClass(request);
        return ResponseEntity.ok(ApiResponse.success(classResponse, "Tạo khối lớp thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> updateClass(@PathVariable Long id, @RequestBody ClassRequest request) {
        ClassResponse classResponse = classService.updateClass(id, request);
        return ResponseEntity.ok(ApiResponse.success(classResponse, "Cập nhật khối lớp thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassById(@PathVariable Long id) {
        ClassResponse classResponse = classService.getClassById(id);
        return ResponseEntity.ok(ApiResponse.success(classResponse));
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassByGrade(@PathVariable Integer grade) {
        ClassResponse classResponse = classService.getClassByGrade(grade);
        return ResponseEntity.ok(ApiResponse.success(classResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getAllClasses() {
        List<ClassResponse> classResponses = classService.getAllClasses();
        return ResponseEntity.ok(ApiResponse.success(classResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa khối lớp thành công"));
    }
}
