package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.enums.ExamType;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExamControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExamService examService;

    @InjectMocks
    private ExamController examController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(examController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/exams - Lưu đề thi thành công")
    void saveExam_Success() throws Exception {
        ExamRequest request = ExamRequest.builder()
                .code("EXAM_KHTN_01")
                .name("Đề thi giữa kỳ KHTN 6")
                .subjectName("Khoa học Tự nhiên 6")
                .type(ExamType.DEFAULT)
                .description("Đề thi giữa kỳ 1")
                .build();

        ExamResponse response = ExamResponse.builder()
                .id(1L)
                .code("EXAM_KHTN_01")
                .name("Đề thi giữa kỳ KHTN 6")
                .subjectName("Khoa học Tự nhiên 6")
                .type(ExamType.DEFAULT)
                .build();

        when(examService.saveExam(any(ExamRequest.class), eq(ExamResponse.class))).thenReturn(response);

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Lưu đề thi thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.code").value("EXAM_KHTN_01"))
                .andExpect(jsonPath("$.data.type").value("DEFAULT"));

        verify(examService, times(1)).saveExam(any(ExamRequest.class), eq(ExamResponse.class));
    }

    @Test
    @DisplayName("POST /api/exams - Lỗi validation khi thiếu mã đề thi")
    void saveExam_ValidationError_BlankCode() throws Exception {
        ExamRequest request = ExamRequest.builder()
                .code("")
                .name("Đề thi giữa kỳ KHTN 6")
                .subjectName("Khoa học Tự nhiên 6")
                .type(ExamType.DEFAULT)
                .build();

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.code").exists());

        verify(examService, never()).saveExam(any(), any());
    }

    @Test
    @DisplayName("POST /api/exams - Lỗi validation khi type là null")
    void saveExam_ValidationError_NullType() throws Exception {
        ExamRequest request = ExamRequest.builder()
                .code("EXAM_01")
                .name("Đề thi giữa kỳ KHTN 6")
                .subjectName("Khoa học Tự nhiên 6")
                .type(null)
                .build();

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.type").exists());

        verify(examService, never()).saveExam(any(), any());
    }

    @Test
    @DisplayName("GET /api/exams - Lấy tất cả đề thi")
    void getAllExams_Success() throws Exception {
        List<ExamResponse> responses = List.of(
                ExamResponse.builder().id(1L).code("EXAM_01").name("Đề 1").build(),
                ExamResponse.builder().id(2L).code("EXAM_02").name("Đề 2").build()
        );

        when(examService.getAllExams()).thenReturn(responses);

        mockMvc.perform(get("/api/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(examService, times(1)).getAllExams();
    }

    @Test
    @DisplayName("GET /api/exams/subject/{subjectName} - Lấy đề thi theo tên môn")
    void getExamsBySubjectName_Success() throws Exception {
        List<ExamResponse> responses = List.of(
                ExamResponse.builder().id(1L).subjectName("Khoa học Tự nhiên 6").build()
        );

        when(examService.getExamsBySubjectName("Khoa học Tự nhiên 6")).thenReturn(responses);

        mockMvc.perform(get("/api/exams/subject/{subjectName}", "Khoa học Tự nhiên 6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(examService, times(1)).getExamsBySubjectName("Khoa học Tự nhiên 6");
    }

    @Test
    @DisplayName("GET /api/exams/type/{type} - Lấy đề thi theo loại đề thi")
    void getExamsByType_Success() throws Exception {
        List<ExamResponse> responses = List.of(
                ExamResponse.builder().id(1L).type(ExamType.DEFAULT).build()
        );

        when(examService.getExamsByType("DEFAULT")).thenReturn(responses);

        mockMvc.perform(get("/api/exams/type/{type}", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(examService, times(1)).getExamsByType("DEFAULT");
    }

    @Test
    @DisplayName("GET /api/exams/name/{name} - Lấy đề thi theo tên đề thi")
    void getExamsByName_Success() throws Exception {
        List<ExamResponse> responses = List.of(
                ExamResponse.builder().id(1L).name("Giữa kỳ").build()
        );

        when(examService.getExamsByName("Giữa kỳ")).thenReturn(responses);

        mockMvc.perform(get("/api/exams/name/{name}", "Giữa kỳ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(examService, times(1)).getExamsByName("Giữa kỳ");
    }

    @Test
    @DisplayName("GET /api/exams/code/{code} - Lấy đề thi theo mã đề")
    void getExamsByCode_Success() throws Exception {
        List<ExamResponse> responses = List.of(
                ExamResponse.builder().id(1L).code("EXAM_01").build()
        );

        when(examService.getExamsByCode("EXAM_01")).thenReturn(responses);

        mockMvc.perform(get("/api/exams/code/{code}", "EXAM_01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(examService, times(1)).getExamsByCode("EXAM_01");
    }

    @Test
    @DisplayName("POST /api/exams - Lỗi khi không tìm thấy đề thi cần cập nhật")
    void saveExam_NotFound() throws Exception {
        ExamRequest request = ExamRequest.builder()
                .id(999L)
                .code("EXAM_999")
                .name("Đề thi không tồn tại")
                .subjectName("Sinh học")
                .type(com.example.exe101_bioverse.exam.enums.ExamType.DEFAULT)
                .build();

        when(examService.saveExam(any(), any()))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.EXAM_NOT_FOUND));

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1605))
                .andExpect(jsonPath("$.message").value("Không tìm thấy đề thi"));

        verify(examService, times(1)).saveExam(any(), any());
    }

    @Test
    @DisplayName("GET /api/exams?page=0&size=10 - Lấy danh mục đề thi phân trang")
    void getExamCatalog_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.response.ExamCatalogResponse item =
                com.example.exe101_bioverse.exam.dto.response.ExamCatalogResponse.builder()
                        .id(1L)
                        .code("DE_GK1")
                        .title("Đề thi GK1")
                        .duration(45)
                        .maxScore(10.0)
                        .status("PUBLISHED")
                        .stats(com.example.exe101_bioverse.exam.dto.response.ExamStatsResponse.builder()
                                .questionCount(40)
                                .totalAssignedPoints(10.0)
                                .build())
                        .build();

        com.example.exe101_bioverse.common.response.PageResponse<com.example.exe101_bioverse.exam.dto.response.ExamCatalogResponse> page =
                com.example.exe101_bioverse.common.response.PageResponse.<com.example.exe101_bioverse.exam.dto.response.ExamCatalogResponse>builder()
                        .items(List.of(item))
                        .totalElements(1)
                        .totalPages(1)
                        .page(0)
                        .size(10)
                        .build();

        when(examService.getExamCatalog(eq(0), eq(10), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/exams")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.items[0].code").value("DE_GK1"));
    }

    @Test
    @DisplayName("GET /api/exams/{id} - Lấy chi tiết đề thi")
    void getExamById_Success() throws Exception {
        ExamResponse response = ExamResponse.builder().id(1L).code("DE_01").name("Đề 1").build();
        when(examService.getExamById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/exams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.code").value("DE_01"));
    }

    @Test
    @DisplayName("PUT /api/exams/{id} - Cập nhật đề thi")
    void updateExam_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.ExamUpdateRequest request =
                com.example.exe101_bioverse.exam.dto.request.ExamUpdateRequest.builder()
                        .name("Đề thi cập nhật")
                        .code("DE_UPDATED")
                        .build();

        ExamResponse response = ExamResponse.builder().id(1L).code("DE_UPDATED").name("Đề thi cập nhật").build();
        when(examService.updateExam(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/exams/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.name").value("Đề thi cập nhật"));
    }

    @Test
    @DisplayName("DELETE /api/exams/{id} - Xóa đề thi")
    void deleteExam_Success() throws Exception {
        doNothing().when(examService).deleteExam(1L);

        mockMvc.perform(delete("/api/exams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa đề thi thành công"));
    }

    @Test
    @DisplayName("POST /api/exams/{id}/duplicate - Nhân bản đề thi")
    void duplicateExam_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.ExamDuplicateRequest request =
                com.example.exe101_bioverse.exam.dto.request.ExamDuplicateRequest.builder()
                        .newCode("DE_02")
                        .newTitle("Đề 2")
                        .build();

        com.example.exe101_bioverse.exam.dto.response.ExamDuplicateResponse response =
                com.example.exe101_bioverse.exam.dto.response.ExamDuplicateResponse.builder()
                        .id(2L)
                        .code("DE_02")
                        .title("Đề 2")
                        .clonedQuestionsCount(40)
                        .build();

        when(examService.duplicateExam(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/exams/{id}/duplicate", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.clonedQuestionsCount").value(40));
    }

    @Test
    @DisplayName("GET /api/exams/{id}/builder - Nạp cấu trúc soạn thảo đề thi")
    void getExamBuilder_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.response.ExamBuilderResponse response =
                com.example.exe101_bioverse.exam.dto.response.ExamBuilderResponse.builder()
                        .exam(com.example.exe101_bioverse.exam.dto.response.ExamBuilderResponse.ExamBasicInfo.builder()
                                .id(1L)
                                .code("DE_01")
                                .title("Đề 1")
                                .build())
                        .summary(com.example.exe101_bioverse.exam.dto.response.ExamBuilderResponse.ExamSummaryInfo.builder()
                                .totalQuestions(1)
                                .totalPoints(10.0)
                                .isValidTotalPoints(true)
                                .build())
                        .questions(List.of())
                        .build();

        when(examService.getExamBuilder(1L)).thenReturn(response);

        mockMvc.perform(get("/api/exams/{id}/builder", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.summary.isValidTotalPoints").value(true));
    }

    @Test
    @DisplayName("PUT /api/exams/{id}/questions/reorder - Sắp xếp lại thứ tự câu hỏi")
    void reorderQuestions_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.ExamQuestionsReorderRequest request =
                com.example.exe101_bioverse.exam.dto.request.ExamQuestionsReorderRequest.builder()
                        .items(List.of(com.example.exe101_bioverse.exam.dto.request.ExamQuestionsReorderRequest.ReorderItem.builder()
                                .examQuestionId(1L)
                                .newOrder(1)
                                .point(0.25)
                                .build()))
                        .build();

        com.example.exe101_bioverse.exam.dto.response.ExamReorderResponse response =
                com.example.exe101_bioverse.exam.dto.response.ExamReorderResponse.builder()
                        .totalQuestions(1)
                        .totalPoints(0.25)
                        .build();

        when(examService.reorderQuestions(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/exams/{id}/questions/reorder", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.totalPoints").value(0.25));
    }

    @Test
    @DisplayName("POST /api/exams/{id}/questions/composite - Thêm câu hỏi nguyên khối kèm đáp án")
    void addCompositeQuestion_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.CompositeQuestionRequest request =
                com.example.exe101_bioverse.exam.dto.request.CompositeQuestionRequest.builder()
                        .content("Đơn vị sống cơ bản là gì?")
                        .point(0.25)
                        .type(QuestionType.SINGLE_CHOICE)
                        .answers(List.of(
                                com.example.exe101_bioverse.exam.dto.request.CompositeQuestionRequest.CompositeAnswerRequest.builder()
                                        .content("Tế bào")
                                        .isCorrect(true)
                                        .build()))
                        .build();

        com.example.exe101_bioverse.exam.dto.response.CompositeQuestionResponse response =
                com.example.exe101_bioverse.exam.dto.response.CompositeQuestionResponse.builder()
                        .examQuestionId(1L)
                        .questionId(10L)
                        .questionOrder(1)
                        .point(0.25)
                        .content("Đơn vị sống cơ bản là gì?")
                        .answersCount(1)
                        .build();

        when(examService.addCompositeQuestion(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/exams/{id}/questions/composite", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.answersCount").value(1));
    }

    @Test
    @DisplayName("DELETE /api/exams/{id}/questions/{questionId} - Gỡ câu hỏi khỏi đề thi")
    void removeQuestionFromExam_Success() throws Exception {
        doNothing().when(examService).removeQuestionFromExam(1L, 10L);

        mockMvc.perform(delete("/api/exams/{id}/questions/{questionId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Gỡ câu hỏi khỏi đề thi thành công"));
    }

    @Test
    @DisplayName("POST /api/exams/{id}/questions/pick-from-bank - Gắn câu hỏi từ ngân hàng")
    void pickQuestionsFromBank_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.PickFromBankRequest request =
                com.example.exe101_bioverse.exam.dto.request.PickFromBankRequest.builder()
                        .questionIds(List.of(10L, 20L))
                        .defaultPoint(0.25)
                        .build();

        com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse r1 =
                com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse.builder().id(1L).point(0.25).build();

        when(examService.pickQuestionsFromBank(eq(1L), any())).thenReturn(List.of(r1));

        mockMvc.perform(post("/api/exams/{id}/questions/pick-from-bank", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));
    }
}
