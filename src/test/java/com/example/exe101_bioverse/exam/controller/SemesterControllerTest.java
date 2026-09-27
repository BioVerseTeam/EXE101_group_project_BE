package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.service.SemesterService;
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
class SemesterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SemesterService semesterService;

    @InjectMocks
    private SemesterController semesterController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(semesterController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/semesters - Tạo học kỳ thành công")
    void createSemester_Success() throws Exception {
        SemesterRequest request = SemesterRequest.builder()
                .classId(1L)
                .name("Học kỳ 1")
                .semesterOrder(1)
                .description("Học kỳ I")
                .build();

        SemesterResponse response = SemesterResponse.builder()
                .id(1L)
                .classId(1L)
                .name("Học kỳ 1")
                .semesterOrder(1)
                .description("Học kỳ I")
                .build();

        when(semesterService.createSemester(any(SemesterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/semesters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Tạo học kỳ thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Học kỳ 1"))
                .andExpect(jsonPath("$.data.semesterOrder").value(1));

        verify(semesterService, times(1)).createSemester(any(SemesterRequest.class));
    }

    @Test
    @DisplayName("POST /api/semesters - Lỗi validation khi classId để trống")
    void createSemester_ValidationError_NullClassId() throws Exception {
        SemesterRequest request = SemesterRequest.builder()
                .name("Học kỳ 1")
                .semesterOrder(1)
                .build();

        mockMvc.perform(post("/api/semesters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.classId").exists());

        verify(semesterService, never()).createSemester(any());
    }

    @Test
    @DisplayName("POST /api/semesters - Lỗi validation khi tên học kỳ trống")
    void createSemester_ValidationError_BlankName() throws Exception {
        SemesterRequest request = SemesterRequest.builder()
                .classId(1L)
                .name("")
                .semesterOrder(1)
                .build();

        mockMvc.perform(post("/api/semesters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.name").exists());

        verify(semesterService, never()).createSemester(any());
    }

    @Test
    @DisplayName("POST /api/semesters - Lỗi validation khi thứ tự học kỳ ngoài 1-2")
    void createSemester_ValidationError_InvalidOrder() throws Exception {
        SemesterRequest request = SemesterRequest.builder()
                .classId(1L)
                .name("Học kỳ 3")
                .semesterOrder(3)
                .build();

        mockMvc.perform(post("/api/semesters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.semesterOrder").exists());

        verify(semesterService, never()).createSemester(any());
    }

    @Test
    @DisplayName("PUT /api/semesters/{id} - Cập nhật học kỳ thành công")
    void updateSemester_Success() throws Exception {
        SemesterRequest request = SemesterRequest.builder()
                .classId(1L)
                .name("Học kỳ 1 Đổi mới")
                .semesterOrder(1)
                .build();

        SemesterResponse response = SemesterResponse.builder()
                .id(1L)
                .name("Học kỳ 1 Đổi mới")
                .semesterOrder(1)
                .build();

        when(semesterService.updateSemester(eq(1L), any(SemesterRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/semesters/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Cập nhật học kỳ thành công"))
                .andExpect(jsonPath("$.data.name").value("Học kỳ 1 Đổi mới"));

        verify(semesterService, times(1)).updateSemester(eq(1L), any(SemesterRequest.class));
    }

    @Test
    @DisplayName("GET /api/semesters/{id} - Lấy chi tiết học kỳ theo ID")
    void getSemesterById_Success() throws Exception {
        SemesterResponse response = SemesterResponse.builder()
                .id(1L)
                .classId(1L)
                .name("Học kỳ 1")
                .semesterOrder(1)
                .build();

        when(semesterService.getSemesterById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/semesters/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Học kỳ 1"));

        verify(semesterService, times(1)).getSemesterById(1L);
    }

    @Test
    @DisplayName("GET /api/semesters/class/{classId} - Lấy danh sách học kỳ theo ID khối lớp")
    void getSemestersByClassId_Success() throws Exception {
        List<SemesterResponse> responses = List.of(
                SemesterResponse.builder().id(1L).name("Học kỳ 1").semesterOrder(1).build(),
                SemesterResponse.builder().id(2L).name("Học kỳ 2").semesterOrder(2).build()
        );

        when(semesterService.getSemestersByClassId(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/semesters/class/{classId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(semesterService, times(1)).getSemestersByClassId(1L);
    }

    @Test
    @DisplayName("GET /api/semesters - Lấy toàn bộ học kỳ")
    void getAllSemesters_Success() throws Exception {
        List<SemesterResponse> responses = List.of(
                SemesterResponse.builder().id(1L).name("Học kỳ 1").semesterOrder(1).build()
        );

        when(semesterService.getAllSemesters()).thenReturn(responses);

        mockMvc.perform(get("/api/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(semesterService, times(1)).getAllSemesters();
    }

    @Test
    @DisplayName("DELETE /api/semesters/{id} - Xóa học kỳ thành công")
    void deleteSemester_Success() throws Exception {
        doNothing().when(semesterService).deleteSemester(1L);

        mockMvc.perform(delete("/api/semesters/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa học kỳ thành công"));

        verify(semesterService, times(1)).deleteSemester(1L);
    }

    @Test
    @DisplayName("GET /api/semesters/{id} - Lỗi khi không tìm thấy học kỳ")
    void getSemesterById_NotFound() throws Exception {
        when(semesterService.getSemesterById(999L))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.SEMESTER_NOT_FOUND));

        mockMvc.perform(get("/api/semesters/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1603))
                .andExpect(jsonPath("$.message").value("Không tìm thấy học kỳ"));

        verify(semesterService, times(1)).getSemesterById(999L);
    }
}
