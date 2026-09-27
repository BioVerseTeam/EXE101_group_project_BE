package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.service.GradeService;
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
class GradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/classes - Thành công khi dữ liệu hợp lệ")
    void createClass_Success() throws Exception {
        ClassRequest request = ClassRequest.builder()
                .name("Lớp 6")
                .grade(6)
                .description("Khối lớp 6")
                .build();

        ClassResponse response = ClassResponse.builder()
                .id(1L)
                .name("Lớp 6")
                .grade(6)
                .description("Khối lớp 6")
                .build();

        when(gradeService.createClass(any(ClassRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Tạo khối lớp thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Lớp 6"))
                .andExpect(jsonPath("$.data.grade").value(6));

        verify(gradeService, times(1)).createClass(any(ClassRequest.class));
    }

    @Test
    @DisplayName("POST /api/classes - Thất bại khi tên khối lớp để trống")
    void createClass_ValidationError_BlankName() throws Exception {
        ClassRequest request = ClassRequest.builder()
                .name("")
                .grade(6)
                .build();

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.message").value("Dữ liệu không hợp lệ"))
                .andExpect(jsonPath("$.data.name").exists());

        verify(gradeService, never()).createClass(any());
    }

    @Test
    @DisplayName("POST /api/classes - Thất bại khi khối lớp ngoài khoảng 6-9")
    void createClass_ValidationError_InvalidGrade() throws Exception {
        ClassRequest request = ClassRequest.builder()
                .name("Lớp 5")
                .grade(5)
                .build();

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400))
                .andExpect(jsonPath("$.data.grade").exists());

        verify(gradeService, never()).createClass(any());
    }

    @Test
    @DisplayName("PUT /api/classes/{id} - Thành công khi dữ liệu hợp lệ")
    void updateClass_Success() throws Exception {
        ClassRequest request = ClassRequest.builder()
                .name("Lớp 6 nâng cao")
                .grade(6)
                .description("Cập nhật")
                .build();

        ClassResponse response = ClassResponse.builder()
                .id(1L)
                .name("Lớp 6 nâng cao")
                .grade(6)
                .build();

        when(gradeService.updateClass(eq(1L), any(ClassRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/classes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Cập nhật khối lớp thành công"))
                .andExpect(jsonPath("$.data.name").value("Lớp 6 nâng cao"));

        verify(gradeService, times(1)).updateClass(eq(1L), any(ClassRequest.class));
    }

    @Test
    @DisplayName("PUT /api/classes/{id} - Thất bại khi tên khối lớp để trống")
    void updateClass_ValidationError_BlankName() throws Exception {
        ClassRequest request = ClassRequest.builder()
                .name("   ")
                .grade(7)
                .build();

        mockMvc.perform(put("/api/classes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400));

        verify(gradeService, never()).updateClass(any(), any());
    }

    @Test
    @DisplayName("GET /api/classes/{id} - Lấy chi tiết khối lớp theo ID")
    void getClassById_Success() throws Exception {
        ClassResponse response = ClassResponse.builder()
                .id(1L)
                .name("Lớp 6")
                .grade(6)
                .build();

        when(gradeService.getClassById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/classes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Lớp 6"));

        verify(gradeService, times(1)).getClassById(1L);
    }

    @Test
    @DisplayName("GET /api/classes/grade/{grade} - Lấy khối lớp theo số grade")
    void getClassByGrade_Success() throws Exception {
        ClassResponse response = ClassResponse.builder()
                .id(2L)
                .name("Lớp 7")
                .grade(7)
                .build();

        when(gradeService.getClassByGrade(7)).thenReturn(response);

        mockMvc.perform(get("/api/classes/grade/{grade}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.grade").value(7));

        verify(gradeService, times(1)).getClassByGrade(7);
    }

    @Test
    @DisplayName("GET /api/classes - Lấy danh sách tất cả các khối lớp")
    void getAllClasses_Success() throws Exception {
        List<ClassResponse> responses = List.of(
                ClassResponse.builder().id(1L).name("Lớp 6").grade(6).build(),
                ClassResponse.builder().id(2L).name("Lớp 7").grade(7).build()
        );

        when(gradeService.getAllClasses()).thenReturn(responses);

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].grade").value(6))
                .andExpect(jsonPath("$.data[1].grade").value(7));

        verify(gradeService, times(1)).getAllClasses();
    }

    @Test
    @DisplayName("DELETE /api/classes/{id} - Xóa khối lớp thành công")
    void deleteClass_Success() throws Exception {
        doNothing().when(gradeService).deleteClass(1L);

        mockMvc.perform(delete("/api/classes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Xóa khối lớp thành công"));

        verify(gradeService, times(1)).deleteClass(1L);
    }

    @Test
    @DisplayName("GET /api/classes/{id} - Lỗi khi không tìm thấy khối lớp")
    void getClassById_NotFound() throws Exception {
        when(gradeService.getClassById(999L))
                .thenThrow(new com.example.exe101_bioverse.common.exception.AppException(
                        com.example.exe101_bioverse.common.exception.ErrorCode.CLASS_NOT_FOUND));

        mockMvc.perform(get("/api/classes/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1601))
                .andExpect(jsonPath("$.message").value("Không tìm thấy khối lớp"));

        verify(gradeService, times(1)).getClassById(999L);
    }
}
