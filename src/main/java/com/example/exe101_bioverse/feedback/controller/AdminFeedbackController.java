package com.example.exe101_bioverse.feedback.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.feedback.dto.request.UpdateFeedbackStatusRequest;
import com.example.exe101_bioverse.feedback.dto.response.FeedbackResponse;
import com.example.exe101_bioverse.feedback.enums.FeedbackCategory;
import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import com.example.exe101_bioverse.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin/feedbacks")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Admin Feedbacks", description = "Quản lý, điều phối ý kiến đóng góp và xuất file báo cáo Excel")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    @Operation(summary = "Tìm kiếm và phân trang danh sách ý kiến đóng góp")
    public ResponseEntity<ApiResponse<PageResponse<FeedbackResponse>>> searchFeedbacks(
            @RequestParam(required = false) FeedbackStatus status,
            @RequestParam(required = false) FeedbackCategory category,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<FeedbackResponse> response = feedbackService.searchFeedbacks(
                status, category, rating, keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết một ý kiến đóng góp")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedback(@PathVariable Long id) {
        FeedbackResponse response = feedbackService.getFeedback(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật trạng thái và ghi chú phản hồi của ban quản trị")
    public ResponseEntity<ApiResponse<FeedbackResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFeedbackStatusRequest request) {

        FeedbackResponse response = feedbackService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật trạng thái góp ý thành công"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một ý kiến đóng góp")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã xóa ý kiến đóng góp"));
    }

    @GetMapping("/export")
    @Operation(summary = "Xuất dữ liệu ý kiến đóng góp ra file Excel (CSV Unicode UTF-8 BOM)")
    public ResponseEntity<byte[]> exportFeedbacks(
            @RequestParam(required = false) FeedbackStatus status,
            @RequestParam(required = false) FeedbackCategory category,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String keyword) {

        byte[] data = feedbackService.exportToCsv(status, category, rating, keyword);
        String filename = "BioVerse_Feedbacks_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .body(data);
    }
}
