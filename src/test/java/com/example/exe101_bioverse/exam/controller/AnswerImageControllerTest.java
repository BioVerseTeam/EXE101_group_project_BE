package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
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
class AnswerImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnswerImageService answerImageService;

    @InjectMocks
    private AnswerImageController answerImageController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(answerImageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/answer-images - Lưu ảnh đáp án thành công")
    void saveAnswerImage_Success() throws Exception {
        AnswerImageRequest request = AnswerImageRequest.builder()
                .answerId(20L)
                .url("https://res.cloudinary.com/demo/image/upload/v1/ans.png")
                .name("Ảnh giải thích đáp án")
                .displayOrder(1)
                .build();

        AnswerImageResponse response = AnswerImageResponse.builder()
                .id(1L)
                .answerId(20L)
                .url("https://res.cloudinary.com/demo/image/upload/v1/ans.png")
                .name("Ảnh giải thích đáp án")
                .displayOrder(1)
                .build();

        when(answerImageService.saveAnswerImage(any(AnswerImageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/answer-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Lưu ảnh đáp án thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.url").value("https://res.cloudinary.com/demo/image/upload/v1/ans.png"));

        verify(answerImageService, times(1)).saveAnswerImage(any(AnswerImageRequest.class));
    }

    @Test
    @DisplayName("POST /api/answer-images - Lỗi validation khi url ảnh để trống")
    void saveAnswerImage_ValidationError_BlankUrl() throws Exception {
        AnswerImageRequest request = AnswerImageRequest.builder()
                .answerId(20L)
                .url("")
                .displayOrder(1)
                .build();

        mockMvc.perform(post("/api/answer-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.url").exists());

        verify(answerImageService, never()).saveAnswerImage(any());
    }

    @Test
    @DisplayName("POST /api/answer-images - Lỗi validation khi thứ tự ảnh nhỏ hơn 1")
    void saveAnswerImage_ValidationError_InvalidOrder() throws Exception {
        AnswerImageRequest request = AnswerImageRequest.builder()
                .answerId(20L)
                .url("https://example.com/ans.png")
                .displayOrder(-1)
                .build();

        mockMvc.perform(post("/api/answer-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.displayOrder").exists());

        verify(answerImageService, never()).saveAnswerImage(any());
    }

    @Test
    @DisplayName("GET /api/answer-images/answer/{answerId} - Lấy ảnh đáp án theo ID đáp án")
    void getAnswerImagesByAnswerId_Success() throws Exception {
        List<AnswerImageResponse> responses = List.of(
                AnswerImageResponse.builder().id(1L).answerId(20L).url("https://example.com/ans.png").build()
        );

        when(answerImageService.getAnswerImagesByAnswerId(20L)).thenReturn(responses);

        mockMvc.perform(get("/api/answer-images/answer/{answerId}", 20L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/ans.png"));

        verify(answerImageService, times(1)).getAnswerImagesByAnswerId(20L);
    }

    @Test
    @DisplayName("POST /api/answer-images - Lỗi khi không tìm thấy ảnh đáp án cần cập nhật")
    void saveAnswerImage_NotFound() throws Exception {
        AnswerImageRequest request = AnswerImageRequest.builder()
                .id(999L)
                .answerId(20L)
                .url("https://example.com/ans.png")
                .displayOrder(1)
                .build();

        when(answerImageService.saveAnswerImage(any()))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.ANSWER_IMAGE_NOT_FOUND));

        mockMvc.perform(post("/api/answer-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1610))
                .andExpect(jsonPath("$.message").value("Không tìm thấy hình ảnh đáp án"));

        verify(answerImageService, times(1)).saveAnswerImage(any());
    }

    @Test
    @DisplayName("DELETE /api/answer-images/{id} - Xóa ảnh đáp án")
    void deleteAnswerImage_Success() throws Exception {
        doNothing().when(answerImageService).deleteAnswerImage(1L);

        mockMvc.perform(delete("/api/answer-images/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa ảnh đáp án thành công"));
    }
}
