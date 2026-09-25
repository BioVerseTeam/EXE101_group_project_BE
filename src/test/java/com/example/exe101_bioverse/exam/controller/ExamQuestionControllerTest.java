package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.exam.service.ExamQuestionService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExamQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExamQuestionService examQuestionService;

    @InjectMocks
    private ExamQuestionController examQuestionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(examQuestionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/exam-questions - Lưu liên kết câu hỏi vào đề thi thành công")
    void saveExamQuestion_Success() throws Exception {
        ExamQuestionRequest request = ExamQuestionRequest.builder()
                .examId(1L)
                .questionId(10L)
                .point(2.5)
                .questionOrder(1)
                .build();

        ExamQuestionResponse response = ExamQuestionResponse.builder()
                .id(1L)
                .examId(1L)
                .questionId(10L)
                .point(2.5)
                .questionOrder(1)
                .build();

        when(examQuestionService.saveExamQuestion(any(ExamQuestionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/exam-questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Lưu câu hỏi vào đề thi thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.examId").value(1))
                .andExpect(jsonPath("$.data.questionId").value(10));

        verify(examQuestionService, times(1)).saveExamQuestion(any(ExamQuestionRequest.class));
    }

    @Test
    @DisplayName("POST /api/exam-questions - Lỗi validation khi thiếu examId")
    void saveExamQuestion_ValidationError_NullExamId() throws Exception {
        ExamQuestionRequest request = ExamQuestionRequest.builder()
                .questionId(10L)
                .point(1.0)
                .build();

        mockMvc.perform(post("/api/exam-questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.examId").exists());

        verify(examQuestionService, never()).saveExamQuestion(any());
    }

    @Test
    @DisplayName("POST /api/exam-questions - Lỗi validation khi điểm số âm")
    void saveExamQuestion_ValidationError_NegativePoint() throws Exception {
        ExamQuestionRequest request = ExamQuestionRequest.builder()
                .examId(1L)
                .questionId(10L)
                .point(-0.5)
                .build();

        mockMvc.perform(post("/api/exam-questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.point").exists());

        verify(examQuestionService, never()).saveExamQuestion(any());
    }

    @Test
    @DisplayName("GET /api/exam-questions/exam/{examId} - Lấy danh sách câu hỏi theo ID đề thi")
    void getExamQuestionsByExamId_Success() throws Exception {
        List<ExamQuestionResponse> responses = List.of(
                ExamQuestionResponse.builder().id(1L).examId(1L).questionId(10L).build(),
                ExamQuestionResponse.builder().id(2L).examId(1L).questionId(11L).build()
        );

        when(examQuestionService.getExamQuestionsByExamId(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/exam-questions/exam/{examId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(examQuestionService, times(1)).getExamQuestionsByExamId(1L);
    }

    @Test
    @DisplayName("GET /api/exam-questions/exam/{examId}/question/{questionId} - Lấy câu hỏi theo ID đề và ID câu")
    void getExamQuestionsByExamIdAndQuestionId_Success() throws Exception {
        List<ExamQuestionResponse> responses = List.of(
                ExamQuestionResponse.builder().id(1L).examId(1L).questionId(10L).build()
        );

        when(examQuestionService.getExamQuestionsByExamIdAndQuestionId(1L, 10L)).thenReturn(responses);

        mockMvc.perform(get("/api/exam-questions/exam/{examId}/question/{questionId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(examQuestionService, times(1)).getExamQuestionsByExamIdAndQuestionId(1L, 10L);
    }

    @Test
    @DisplayName("POST /api/exam-questions - Lỗi khi không tìm thấy liên kết câu hỏi - đề thi")
    void saveExamQuestion_NotFound() throws Exception {
        ExamQuestionRequest request = ExamQuestionRequest.builder()
                .id(999L)
                .examId(1L)
                .questionId(10L)
                .point(2.0)
                .questionOrder(1)
                .build();

        when(examQuestionService.saveExamQuestion(any()))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.EXAM_QUESTION_NOT_FOUND));

        mockMvc.perform(post("/api/exam-questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1608))
                .andExpect(jsonPath("$.message").value("Không tìm thấy câu hỏi trong đề thi"));

        verify(examQuestionService, times(1)).saveExamQuestion(any());
    }
}
