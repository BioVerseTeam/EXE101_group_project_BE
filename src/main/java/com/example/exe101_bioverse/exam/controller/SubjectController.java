package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;
import com.example.exe101_bioverse.exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@RequestBody SubjectRequest request) {
        SubjectResponse subjectResponse = subjectService.createSubject(request);
        return ResponseEntity.ok(ApiResponse.success(subjectResponse, "Tạo môn học thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable Long id, @RequestBody SubjectRequest request) {
        SubjectResponse subjectResponse = subjectService.updateSubject(id, request);
        return ResponseEntity.ok(ApiResponse.success(subjectResponse, "Cập nhật môn học thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
        SubjectResponse subjectResponse = subjectService.getSubjectById(id);
        return ResponseEntity.ok(ApiResponse.success(subjectResponse));
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjectsBySemesterId(@PathVariable Long semesterId) {
        List<SubjectResponse> subjectResponses = subjectService.getSubjectsBySemesterId(semesterId);
        return ResponseEntity.ok(ApiResponse.success(subjectResponses));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        List<SubjectResponse> subjectResponses = subjectService.getAllSubjects();
        return ResponseEntity.ok(ApiResponse.success(subjectResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa môn học thành công"));
    }
}
