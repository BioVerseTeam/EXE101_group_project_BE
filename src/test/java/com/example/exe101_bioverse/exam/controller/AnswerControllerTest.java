package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.enums.AnswerType;
import com.example.exe101_bioverse.exam.service.AnswerService;
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
class AnswerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(answerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/answers - Lưu đáp án thành công")
    void saveAnswer_Success() throws Exception {
        AnswerRequest request = AnswerRequest.builder()
                .questionId(10L)
                .content("Màng tế bào")
                .type(AnswerType.TEXT)
                .isCorrect(true)
                .explain("Màng tế bào bao bọc và bảo vệ tế bào")
                .build();

        AnswerResponse response = AnswerResponse.builder()
                .id(1L)
                .questionId(10L)
                .content("Màng tế bào")
                .type(AnswerType.TEXT)
                .isCorrect(true)
                .build();

        when(answerService.saveAnswer(any(AnswerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Màng tế bào"))
                .andExpect(jsonPath("$.data.isCorrect").value(true));

        verify(answerService, times(1)).saveAnswer(any(AnswerRequest.class));
    }

    @Test
    @DisplayName("POST /api/answers - Lỗi validation khi nội dung đáp án trống")
    void saveAnswer_ValidationError_BlankContent() throws Exception {
        AnswerRequest request = AnswerRequest.builder()
                .questionId(10L)
                .content("")
                .type(AnswerType.TEXT)
                .isCorrect(false)
                .build();

        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.content").exists());

        verify(answerService, never()).saveAnswer(any());
    }

    @Test
    @DisplayName("POST /api/answers - Lỗi validation khi type của đáp án null")
    void saveAnswer_ValidationError_NullType() throws Exception {
        AnswerRequest request = AnswerRequest.builder()
                .questionId(10L)
                .content("Đáp án B")
                .type(null)
                .isCorrect(false)
                .build();

        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.type").exists());

        verify(answerService, never()).saveAnswer(any());
    }

    @Test
    @DisplayName("GET /api/answers/question/{questionId} - Lấy danh sách đáp án theo ID câu hỏi")
    void getAnswersByQuestionId_Success() throws Exception {
        List<AnswerResponse> responses = List.of(
                AnswerResponse.builder().id(1L).content("Đáp án A").isCorrect(true).build(),
                AnswerResponse.builder().id(2L).content("Đáp án B").isCorrect(false).build()
        );

        when(answerService.getAnswersByQuestionId(10L)).thenReturn(responses);

        mockMvc.perform(get("/api/answers/question/{questionId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].content").value("Đáp án A"));

        verify(answerService, times(1)).getAnswersByQuestionId(10L);
    }

    @Test
    @DisplayName("POST /api/answers - Lỗi khi không tìm thấy đáp án cần cập nhật")
    void saveAnswer_NotFound() throws Exception {
        AnswerRequest request = AnswerRequest.builder()
                .id(999L)
                .questionId(10L)
                .content("Màng tế bào")
                .type(AnswerType.TEXT)
                .isCorrect(true)
                .build();

        when(answerService.saveAnswer(any()))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.ANSWER_NOT_FOUND));

        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1607))
                .andExpect(jsonPath("$.message").value("Không tìm thấy đáp án"));

        verify(answerService, times(1)).saveAnswer(any());
    }

    @Test
    @DisplayName("PUT /api/answers/{id} - Cập nhật đáp án")
    void updateAnswer_Success() throws Exception {
        AnswerRequest request = AnswerRequest.builder()
                .content("Đáp án đã sửa")
                .type(AnswerType.TEXT)
                .isCorrect(true)
                .build();

        AnswerResponse response = AnswerResponse.builder()
                .id(1L)
                .content("Đáp án đã sửa")
                .isCorrect(true)
                .build();

        when(answerService.updateAnswer(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/answers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.content").value("Đáp án đã sửa"));
    }

    @Test
    @DisplayName("DELETE /api/answers/{id} - Xóa đáp án")
    void deleteAnswer_Success() throws Exception {
        doNothing().when(answerService).deleteAnswer(1L);

        mockMvc.perform(delete("/api/answers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa đáp án thành công"));
    }
}
