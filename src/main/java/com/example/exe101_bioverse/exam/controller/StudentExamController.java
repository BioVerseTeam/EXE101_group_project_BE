package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.request.StudentExamSubmitRequest;
import com.example.exe101_bioverse.exam.dto.response.StudentExamAttemptDetailResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamHistoryResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamPaperResponse;
import com.example.exe101_bioverse.exam.dto.response.StudentExamSubmitResponse;
import com.example.exe101_bioverse.exam.service.StudentExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student Exam Flow", description = "Các API làm bài thi, nộp bài, chống gian lận và xem lịch sử thi của học sinh")
public class StudentExamController {

    private final StudentExamService studentExamService;

    @GetMapping("/exams/{id}/paper")
    @Operation(summary = "Lấy đề thi làm bài chống gian lận (Anti-Cheat Paper)")
    public ResponseEntity<ApiResponse<StudentExamPaperResponse>> getAntiCheatExamPaper(
            @PathVariable("id") Long examId) {
        StudentExamPaperResponse response = studentExamService.getAntiCheatExamPaper(examId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/exams/{id}/submit")
    @Operation(summary = "Học sinh nộp bài thi - Server tự động chấm điểm và tính XP")
    public ResponseEntity<ApiResponse<StudentExamSubmitResponse>> submitExam(
            @PathVariable("id") Long examId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody StudentExamSubmitRequest request) {
        Long userId = (principal != null) ? principal.getId() : null;
        StudentExamSubmitResponse response = studentExamService.submitExam(examId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/exam-attempts/{attemptId}")
    @Operation(summary = "Xem lại chi tiết bài làm, đáp án đúng và lời giải thích")
    public ResponseEntity<ApiResponse<StudentExamAttemptDetailResponse>> getExamAttemptDetail(
            @PathVariable("attemptId") Long attemptId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : null;
        StudentExamAttemptDetailResponse response = studentExamService.getExamAttemptDetail(attemptId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/exam-attempts/my-history")
    @Operation(summary = "Xem lịch sử thi và thống kê tiến độ học tập của học sinh")
    public ResponseEntity<ApiResponse<StudentExamHistoryResponse>> getMyHistory(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : null;
        StudentExamHistoryResponse response = studentExamService.getStudentHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
