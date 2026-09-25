package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.QuestionService;
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
class QuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;

    @Mock
    private com.example.exe101_bioverse.exam.service.AnswerService answerService;

    @InjectMocks
    private QuestionController questionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/questions - Lưu câu hỏi thành công")
    void saveQuestion_Success() throws Exception {
        QuestionRequest request = QuestionRequest.builder()
                .content("Tế bào là gì?")
                .type(QuestionType.MULTIPLE_CHOICE)
                .point(1.0)
                .questionOrder(1)
                .explain("Đơn vị cơ bản của sự sống")
                .build();

        QuestionResponse response = QuestionResponse.builder()
                .id(1L)
                .content("Tế bào là gì?")
                .type(QuestionType.MULTIPLE_CHOICE)
                .build();

        when(questionService.saveQuestion(any(QuestionRequest.class), eq(QuestionResponse.class))).thenReturn(response);

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Lưu câu hỏi thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Tế bào là gì?"))
                .andExpect(jsonPath("$.data.type").value("MULTIPLE_CHOICE"));

        verify(questionService, times(1)).saveQuestion(any(QuestionRequest.class), eq(QuestionResponse.class));
    }

    @Test
    @DisplayName("POST /api/questions - Lỗi validation khi content trống")
    void saveQuestion_ValidationError_BlankContent() throws Exception {
        QuestionRequest request = QuestionRequest.builder()
                .content("")
                .type(QuestionType.MULTIPLE_CHOICE)
                .point(1.0)
                .questionOrder(1)
                .build();

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.content").exists());

        verify(questionService, never()).saveQuestion(any(), any());
    }

    @Test
    @DisplayName("POST /api/questions - Lỗi validation khi điểm số âm")
    void saveQuestion_ValidationError_NegativePoint() throws Exception {
        QuestionRequest request = QuestionRequest.builder()
                .content("Câu hỏi hợp lệ")
                .type(QuestionType.MULTIPLE_CHOICE)
                .point(-2.0)
                .questionOrder(1)
                .build();

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.point").exists());

        verify(questionService, never()).saveQuestion(any(), any());
    }

    @Test
    @DisplayName("GET /api/questions/{id} - Lấy câu hỏi theo ID")
    void getQuestionById_Success() throws Exception {
        QuestionResponse response = QuestionResponse.builder()
                .id(1L)
                .content("Tế bào là gì?")
                .type(QuestionType.MULTIPLE_CHOICE)
                .build();

        when(questionService.getQuestionById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/questions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Tế bào là gì?"));

        verify(questionService, times(1)).getQuestionById(1L);
    }

    @Test
    @DisplayName("GET /api/questions/exam/{examId} - Lấy danh sách câu hỏi theo ID đề thi")
    void getQuestionsByExamId_Success() throws Exception {
        List<QuestionResponse> responses = List.of(
                QuestionResponse.builder().id(1L).content("Câu 1").build(),
                QuestionResponse.builder().id(2L).content("Câu 2").build()
        );

        when(questionService.getQuestionsByExamId(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/questions/exam/{examId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(questionService, times(1)).getQuestionsByExamId(1L);
    }

    @Test
    @DisplayName("GET /api/questions/type/{questionType} - Lấy câu hỏi theo loại")
    void getQuestionByType_Success() throws Exception {
        List<QuestionResponse> responses = List.of(
                QuestionResponse.builder().id(1L).type(QuestionType.MULTIPLE_CHOICE).build()
        );

        when(questionService.getQuestionByType(QuestionType.MULTIPLE_CHOICE)).thenReturn(responses);

        mockMvc.perform(get("/api/questions/type/{questionType}", "MULTIPLE_CHOICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(questionService, times(1)).getQuestionByType(QuestionType.MULTIPLE_CHOICE);
    }

    @Test
    @DisplayName("GET /api/questions/{id} - Lỗi khi không tìm thấy câu hỏi")
    void getQuestionById_NotFound() throws Exception {
        when(questionService.getQuestionById(999L))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.QUESTION_NOT_FOUND));

        mockMvc.perform(get("/api/questions/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1606))
                .andExpect(jsonPath("$.message").value("Không tìm thấy câu hỏi"));

        verify(questionService, times(1)).getQuestionById(999L);
    }

    @Test
    @DisplayName("GET /api/questions/bank - Tìm kiếm ngân hàng câu hỏi phân trang")
    void getQuestionBank_Success() throws Exception {
        QuestionResponse q1 = QuestionResponse.builder().id(1L).content("Câu hỏi 1").build();
        com.example.exe101_bioverse.common.response.PageResponse<QuestionResponse> page =
                com.example.exe101_bioverse.common.response.PageResponse.<QuestionResponse>builder()
                        .items(List.of(q1))
                        .totalElements(1)
                        .totalPages(1)
                        .page(0)
                        .size(10)
                        .build();

        when(questionService.getQuestionBank(any(), any(), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/api/questions/bank")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.items[0].content").value("Câu hỏi 1"));
    }

    @Test
    @DisplayName("PUT /api/questions/{id} - Cập nhật câu hỏi")
    void updateQuestion_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.QuestionUpdateRequest request =
                com.example.exe101_bioverse.exam.dto.request.QuestionUpdateRequest.builder()
                        .content("Nội dung mới")
                        .type(QuestionType.SINGLE_CHOICE)
                        .build();

        QuestionResponse response = QuestionResponse.builder()
                .id(1L)
                .content("Nội dung mới")
                .type(QuestionType.SINGLE_CHOICE)
                .build();

        when(questionService.updateQuestion(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/questions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.content").value("Nội dung mới"));
    }

    @Test
    @DisplayName("DELETE /api/questions/{id} - Xóa câu hỏi")
    void deleteQuestion_Success() throws Exception {
        doNothing().when(questionService).deleteQuestion(1L);

        mockMvc.perform(delete("/api/questions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa câu hỏi thành công"));
    }

    @Test
    @DisplayName("POST /api/questions/{questionId}/answers - Thêm đáp án cho câu hỏi")
    void addAnswerToQuestion_Success() throws Exception {
        com.example.exe101_bioverse.exam.dto.request.AnswerRequest request =
                com.example.exe101_bioverse.exam.dto.request.AnswerRequest.builder()
                        .content("Đáp án A")
                        .type(com.example.exe101_bioverse.exam.enums.AnswerType.TEXT)
                        .isCorrect(true)
                        .build();

        com.example.exe101_bioverse.exam.dto.response.AnswerResponse response =
                com.example.exe101_bioverse.exam.dto.response.AnswerResponse.builder()
                        .id(1L)
                        .content("Đáp án A")
                        .isCorrect(true)
                        .build();

        when(answerService.addAnswerToQuestion(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/questions/{questionId}/answers", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.content").value("Đáp án A"));
    }
}
