package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private R2StorageService r2StorageService;

    @InjectMocks
    private MediaController mediaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mediaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/media/upload - Upload ảnh thành công")
    void uploadImage_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "cell.png", "image/png", "fake image data".getBytes());

        ModelAssetResponse r2Response = new ModelAssetResponse(
                "exams/questions/cell.png", 15L, "https://storage.bioverse.edu.vn/exams/questions/cell.png");

        when(r2StorageService.uploadObject(anyString(), any(), anyString())).thenReturn(r2Response);

        mockMvc.perform(multipart("/api/media/upload")
                        .file(file)
                        .param("folder", "exams/questions"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Upload hình ảnh thành công"))
                .andExpect(jsonPath("$.data.url").value("https://storage.bioverse.edu.vn/exams/questions/cell.png"))
                .andExpect(jsonPath("$.data.fileName").value("cell.png"));

        verify(r2StorageService, times(1)).uploadObject(anyString(), any(), eq("image/png"));
    }

    @Test
    @DisplayName("POST /api/media/upload - Lỗi khi không truyền file")
    void uploadImage_EmptyFile_Error() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "", "image/png", new byte[0]);

        mockMvc.perform(multipart("/api/media/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1410));

        verify(r2StorageService, never()).uploadObject(any(), any(), any());
    }

    @Test
    @DisplayName("POST /api/media/upload - Lỗi khi định dạng không được hỗ trợ")
    void uploadImage_UnsupportedFormat_Error() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "doc.pdf", "application/pdf", "fake pdf".getBytes());

        mockMvc.perform(multipart("/api/media/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1410));

        verify(r2StorageService, never()).uploadObject(any(), any(), any());
    }
}
