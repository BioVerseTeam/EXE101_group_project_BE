package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.*;
import com.example.exe101_bioverse.exam.dto.response.*;
import com.example.exe101_bioverse.exam.service.ExamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@Validated
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResponse>> saveExam(@Valid @RequestBody ExamRequest examRequest) {
        ExamResponse examResponse = examService.saveExam(examRequest, ExamResponse.class);
        return ResponseEntity.ok(ApiResponse.success(examResponse, "Lưu đề thi thành công"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getExams(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Integer grade,
            @RequestParam(required = false) String sort) {
        if (page != null || (search != null && !search.isBlank()) || subjectId != null || grade != null) {
            PageResponse<ExamCatalogResponse> catalog = examService.getExamCatalog(
                    page != null ? page : 0, size != null ? size : 12, search, subjectId, grade, sort);
            return ResponseEntity.ok(ApiResponse.success(catalog, "Lấy danh sách đề thi thành công"));
        }
        List<ExamResponse> examResponses = examService.getAllExams();
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> getExamById(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id) {
        ExamResponse examResponse = examService.getExamById(id);
        return ResponseEntity.ok(ApiResponse.success(examResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> updateExam(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id,
            @Valid @RequestBody ExamUpdateRequest request) {
        ExamResponse updated = examService.updateExam(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật đề thi thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExam(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa đề thi thành công"));
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<ApiResponse<ExamDuplicateResponse>> duplicateExam(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id,
            @Valid @RequestBody ExamDuplicateRequest request) {
        ExamDuplicateResponse duplicated = examService.duplicateExam(id, request);
        return ResponseEntity.ok(ApiResponse.success(duplicated, "Nhân bản đề thi thành công"));
    }

    @GetMapping("/{id}/builder")
    public ResponseEntity<ApiResponse<ExamBuilderResponse>> getExamBuilder(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id) {
        ExamBuilderResponse builder = examService.getExamBuilder(id);
        return ResponseEntity.ok(ApiResponse.success(builder, "Nạp dữ liệu đề thi thành công"));
    }

    @PutMapping("/{id}/questions/reorder")
    public ResponseEntity<ApiResponse<ExamReorderResponse>> reorderQuestions(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id,
            @Valid @RequestBody ExamQuestionsReorderRequest request) {
        ExamReorderResponse reorderResponse = examService.reorderQuestions(id, request);
        return ResponseEntity.ok(ApiResponse.success(reorderResponse, "Cập nhật thứ tự và phân bổ điểm thành công"));
    }

    @PostMapping(value = {"/{id}/questions/composite", "/{id}/questions"})
    public ResponseEntity<ApiResponse<CompositeQuestionResponse>> addCompositeQuestion(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id,
            @Valid @RequestBody CompositeQuestionRequest request) {
        CompositeQuestionResponse response = examService.addCompositeQuestion(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Thêm câu hỏi vào đề thi thành công"));
    }

    @DeleteMapping("/{id}/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> removeQuestionFromExam(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long examId,
            @PathVariable("questionId") @Positive(message = "ID câu hỏi phải lớn hơn 0") Long questionId) {
        examService.removeQuestionFromExam(examId, questionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Gỡ câu hỏi khỏi đề thi thành công"));
    }

    @PostMapping("/{id}/questions/pick-from-bank")
    public ResponseEntity<ApiResponse<List<ExamQuestionResponse>>> pickQuestionsFromBank(
            @PathVariable("id") @Positive(message = "ID đề thi phải lớn hơn 0") Long id,
            @Valid @RequestBody PickFromBankRequest request) {
        List<ExamQuestionResponse> responses = examService.pickQuestionsFromBank(id, request);
        return ResponseEntity.ok(ApiResponse.success(responses, "Gắn câu hỏi từ ngân hàng thành công"));
    }

    @GetMapping("/subject/{subjectName}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsBySubjectName(
            @PathVariable @NotBlank(message = "Tên môn học không được để trống") String subjectName) {
        List<ExamResponse> examResponses = examService.getExamsBySubjectName(subjectName);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByType(
            @PathVariable @NotBlank(message = "Loại đề thi không được để trống") String type) {
        List<ExamResponse> examResponses = examService.getExamsByType(type);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByName(
            @PathVariable @NotBlank(message = "Tên đề thi không được để trống") String name) {
        List<ExamResponse> examResponses = examService.getExamsByName(name);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByCode(
            @PathVariable @NotBlank(message = "Mã đề thi không được để trống") String code) {
        List<ExamResponse> examResponses = examService.getExamsByCode(code);
        return ResponseEntity.ok(ApiResponse.success(examResponses));
    }
}
