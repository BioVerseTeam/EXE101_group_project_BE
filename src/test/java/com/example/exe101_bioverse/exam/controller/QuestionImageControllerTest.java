package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
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
class QuestionImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionImageService questionImageService;

    @InjectMocks
    private QuestionImageController questionImageController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionImageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/question-images - Lưu ảnh câu hỏi thành công")
    void saveQuestionImage_Success() throws Exception {
        QuestionImageRequest request = QuestionImageRequest.builder()
                .questionId(10L)
                .url("https://res.cloudinary.com/demo/image/upload/v1/cell.png")
                .name("Hình tế bào thực vật")
                .displayOrder(1)
                .build();

        QuestionImageResponse response = QuestionImageResponse.builder()
                .id(1L)
                .questionId(10L)
                .url("https://res.cloudinary.com/demo/image/upload/v1/cell.png")
                .name("Hình tế bào thực vật")
                .displayOrder(1)
                .build();

        when(questionImageService.saveQuestionImage(any(QuestionImageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/question-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Lưu ảnh câu hỏi thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.url").value("https://res.cloudinary.com/demo/image/upload/v1/cell.png"));

        verify(questionImageService, times(1)).saveQuestionImage(any(QuestionImageRequest.class));
    }

    @Test
    @DisplayName("POST /api/question-images - Lỗi validation khi url ảnh trống")
    void saveQuestionImage_ValidationError_BlankUrl() throws Exception {
        QuestionImageRequest request = QuestionImageRequest.builder()
                .questionId(10L)
                .url("")
                .displayOrder(1)
                .build();

        mockMvc.perform(post("/api/question-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.url").exists());

        verify(questionImageService, never()).saveQuestionImage(any());
    }

    @Test
    @DisplayName("POST /api/question-images - Lỗi validation khi thứ tự ảnh nhỏ hơn 1")
    void saveQuestionImage_ValidationError_InvalidOrder() throws Exception {
        QuestionImageRequest request = QuestionImageRequest.builder()
                .questionId(10L)
                .url("https://example.com/img.png")
                .displayOrder(0)
                .build();

        mockMvc.perform(post("/api/question-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.displayOrder").exists());

        verify(questionImageService, never()).saveQuestionImage(any());
    }

    @Test
    @DisplayName("GET /api/question-images/question/{questionId} - Lấy ảnh câu hỏi theo ID câu hỏi")
    void getQuestionImagesByQuestionId_Success() throws Exception {
        List<QuestionImageResponse> responses = List.of(
                QuestionImageResponse.builder().id(1L).questionId(10L).url("https://example.com/img1.png").build()
        );

        when(questionImageService.getQuestionImagesByQuestionId(10L)).thenReturn(responses);

        mockMvc.perform(get("/api/question-images/question/{questionId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/img1.png"));

        verify(questionImageService, times(1)).getQuestionImagesByQuestionId(10L);
    }

    @Test
    @DisplayName("POST /api/question-images - Lỗi khi không tìm thấy ảnh câu hỏi cần cập nhật")
    void saveQuestionImage_NotFound() throws Exception {
        QuestionImageRequest request = QuestionImageRequest.builder()
                .id(999L)
                .questionId(10L)
                .url("https://example.com/cell.png")
                .displayOrder(1)
                .build();

        when(questionImageService.saveQuestionImage(any()))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.QUESTION_IMAGE_NOT_FOUND));

        mockMvc.perform(post("/api/question-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1609))
                .andExpect(jsonPath("$.message").value("Không tìm thấy hình ảnh câu hỏi"));

        verify(questionImageService, times(1)).saveQuestionImage(any());
    }

    @Test
    @DisplayName("DELETE /api/question-images/{id} - Xóa ảnh câu hỏi")
    void deleteQuestionImage_Success() throws Exception {
        doNothing().when(questionImageService).deleteQuestionImage(1L);

        mockMvc.perform(delete("/api/question-images/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa ảnh câu hỏi thành công"));
    }
}
