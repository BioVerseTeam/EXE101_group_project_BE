package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;
import com.example.exe101_bioverse.exam.service.SubjectService;
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
class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/subjects - Tạo môn học thành công")
    void createSubject_Success() throws Exception {
        SubjectRequest request = SubjectRequest.builder()
                .semesterId(1L)
                .name("Khoa học Tự nhiên 6")
                .code("KHTN6")
                .description("Môn KHTN lớp 6")
                .build();

        SubjectResponse response = SubjectResponse.builder()
                .id(1L)
                .semesterId(1L)
                .name("Khoa học Tự nhiên 6")
                .code("KHTN6")
                .description("Môn KHTN lớp 6")
                .build();

        when(subjectService.createSubject(any(SubjectRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Tạo môn học thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Khoa học Tự nhiên 6"))
                .andExpect(jsonPath("$.data.code").value("KHTN6"));

        verify(subjectService, times(1)).createSubject(any(SubjectRequest.class));
    }

    @Test
    @DisplayName("POST /api/subjects - Lỗi validation khi semesterId để trống")
    void createSubject_ValidationError_NullSemesterId() throws Exception {
        SubjectRequest request = SubjectRequest.builder()
                .name("Khoa học Tự nhiên 6")
                .code("KHTN6")
                .build();

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.semesterId").exists());

        verify(subjectService, never()).createSubject(any());
    }

    @Test
    @DisplayName("POST /api/subjects - Lỗi validation khi code môn học để trống")
    void createSubject_ValidationError_BlankCode() throws Exception {
        SubjectRequest request = SubjectRequest.builder()
                .semesterId(1L)
                .name("Khoa học Tự nhiên 6")
                .code("")
                .build();

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.code").exists());

        verify(subjectService, never()).createSubject(any());
    }

    @Test
    @DisplayName("PUT /api/subjects/{id} - Cập nhật môn học thành công")
    void updateSubject_Success() throws Exception {
        SubjectRequest request = SubjectRequest.builder()
                .semesterId(1L)
                .name("Sinh học 6 nâng cao")
                .code("BIO6")
                .build();

        SubjectResponse response = SubjectResponse.builder()
                .id(1L)
                .name("Sinh học 6 nâng cao")
                .code("BIO6")
                .build();

        when(subjectService.updateSubject(eq(1L), any(SubjectRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/subjects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Cập nhật môn học thành công"))
                .andExpect(jsonPath("$.data.name").value("Sinh học 6 nâng cao"));

        verify(subjectService, times(1)).updateSubject(eq(1L), any(SubjectRequest.class));
    }

    @Test
    @DisplayName("GET /api/subjects/{id} - Lấy chi tiết môn học theo ID")
    void getSubjectById_Success() throws Exception {
        SubjectResponse response = SubjectResponse.builder()
                .id(1L)
                .semesterId(1L)
                .name("Khoa học Tự nhiên 6")
                .code("KHTN6")
                .build();

        when(subjectService.getSubjectById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/subjects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.code").value("KHTN6"));

        verify(subjectService, times(1)).getSubjectById(1L);
    }

    @Test
    @DisplayName("GET /api/subjects/semester/{semesterId} - Lấy danh sách môn học theo ID học kỳ")
    void getSubjectsBySemesterId_Success() throws Exception {
        List<SubjectResponse> responses = List.of(
                SubjectResponse.builder().id(1L).code("KHTN6").name("Khoa học Tự nhiên 6").build()
        );

        when(subjectService.getSubjectsBySemesterId(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/subjects/semester/{semesterId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(subjectService, times(1)).getSubjectsBySemesterId(1L);
    }

    @Test
    @DisplayName("GET /api/subjects - Lấy toàn bộ danh sách môn học")
    void getAllSubjects_Success() throws Exception {
        List<SubjectResponse> responses = List.of(
                SubjectResponse.builder().id(1L).code("KHTN6").name("Khoa học Tự nhiên 6").build()
        );

        when(subjectService.getAllSubjects()).thenReturn(responses);

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(subjectService, times(1)).getAllSubjects();
    }

    @Test
    @DisplayName("DELETE /api/subjects/{id} - Xóa môn học thành công")
    void deleteSubject_Success() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/api/subjects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa môn học thành công"));

        verify(subjectService, times(1)).deleteSubject(1L);
    }

    @Test
    @DisplayName("GET /api/subjects/{id} - Lỗi khi không tìm thấy môn học")
    void getSubjectById_NotFound() throws Exception {
        when(subjectService.getSubjectById(999L))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.SUBJECT_NOT_FOUND));

        mockMvc.perform(get("/api/subjects/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1604))
                .andExpect(jsonPath("$.message").value("Không tìm thấy môn học"));

        verify(subjectService, times(1)).getSubjectById(999L);
    }
}
