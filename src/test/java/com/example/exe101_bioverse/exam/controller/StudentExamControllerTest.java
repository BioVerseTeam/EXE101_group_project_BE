package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.example.exe101_bioverse.exam.dto.request.StudentExamSubmitRequest;
import com.example.exe101_bioverse.exam.dto.request.StudentSubmittedAnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.*;
import com.example.exe101_bioverse.exam.enums.AnswerType;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.service.StudentExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentExamControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private StudentExamService studentExamService;

    @InjectMocks
    private StudentExamController studentExamController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentExamController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET /api/student/exams/{id}/paper - Lấy đề thi Anti-Cheat thành công")
    void getAntiCheatExamPaper_Success() throws Exception {
        StudentExamPaperResponse paperResponse = StudentExamPaperResponse.builder()
                .examId(1L)
                .code("DE_GK1_KHTN6_01")
                .name("Đề thi giữa kì 1 KHTN 6")
                .durationMinutes(45)
                .totalScore(10.0)
                .totalQuestions(1)
                .questions(List.of(
                        StudentQuestionPaperResponse.builder()
                                .id(100L)
                                .questionOrder(1)
                                .type(QuestionType.SINGLE_CHOICE)
                                .point(0.25)
                                .content("Câu hỏi 1?")
                                .answers(List.of(
                                        StudentAnswerPaperResponse.builder()
                                                .id(200L)
                                                .type(AnswerType.TEXT)
                                                .content("Đáp án A")
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(studentExamService.getAntiCheatExamPaper(1L)).thenReturn(paperResponse);

        mockMvc.perform(get("/api/student/exams/1/paper"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.examId").value(1))
                .andExpect(jsonPath("$.data.code").value("DE_GK1_KHTN6_01"))
                .andExpect(jsonPath("$.data.questions[0].id").value(100))
                .andExpect(jsonPath("$.data.questions[0].isCorrect").doesNotExist())
                .andExpect(jsonPath("$.data.questions[0].explanation").doesNotExist())
                .andExpect(jsonPath("$.data.questions[0].answers[0].isCorrect").doesNotExist());

        verify(studentExamService, times(1)).getAntiCheatExamPaper(1L);
    }

    @Test
    @DisplayName("GET /api/student/exams/{id}/paper - Thất bại khi không tìm thấy đề thi (404)")
    void getAntiCheatExamPaper_NotFound() throws Exception {
        when(studentExamService.getAntiCheatExamPaper(999L))
                .thenThrow(new AppException(ErrorCode.EXAM_NOT_FOUND));

        mockMvc.perform(get("/api/student/exams/999/paper"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1605));

        verify(studentExamService, times(1)).getAntiCheatExamPaper(999L);
    }

    @Test
    @DisplayName("POST /api/student/exams/{id}/submit - Nộp bài thi thành công và server chấm điểm")
    void submitExam_Success() throws Exception {
        StudentExamSubmitRequest request = StudentExamSubmitRequest.builder()
                .timeSpentSec(1800)
                .startedAt(LocalDateTime.now().minusMinutes(30))
                .answers(List.of(
                        StudentSubmittedAnswerRequest.builder()
                                .questionId(100L)
                                .selectedAnswerId(200L)
                                .timeSpentSec(45)
                                .build()
                ))
                .build();

        StudentExamSubmitResponse submitResponse = StudentExamSubmitResponse.builder()
                .attemptId(10L)
                .examId(1L)
                .examTitle("Đề thi giữa kì 1 KHTN 6")
                .score(10.0)
                .totalQuestions(1)
                .correctCount(1)
                .timeSpentSec(1800)
                .earnedXp(120)
                .submittedAt(LocalDateTime.now())
                .feedback("Xuất sắc!")
                .build();

        when(studentExamService.submitExam(eq(1L), any(), any(StudentExamSubmitRequest.class)))
                .thenReturn(submitResponse);

        mockMvc.perform(post("/api/student/exams/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.attemptId").value(10))
                .andExpect(jsonPath("$.data.score").value(10.0))
                .andExpect(jsonPath("$.data.earnedXp").value(120))
                .andExpect(jsonPath("$.data.correctCount").value(1));

        verify(studentExamService, times(1)).submitExam(eq(1L), any(), any(StudentExamSubmitRequest.class));
    }

    @Test
    @DisplayName("POST /api/student/exams/{id}/submit - Thất bại validation khi questionId null")
    void submitExam_ValidationError() throws Exception {
        StudentExamSubmitRequest invalidRequest = StudentExamSubmitRequest.builder()
                .answers(List.of(
                        StudentSubmittedAnswerRequest.builder()
                                .questionId(null)
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/student/exams/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1400));

        verify(studentExamService, never()).submitExam(any(), any(), any());
    }

    @Test
    @DisplayName("GET /api/student/exam-attempts/{attemptId} - Xem lại bài làm thành công")
    void getExamAttemptDetail_Success() throws Exception {
        StudentExamAttemptDetailResponse detailResponse = StudentExamAttemptDetailResponse.builder()
                .attemptId(10L)
                .examId(1L)
                .examCode("DE_GK1_KHTN6_01")
                .examTitle("Đề thi giữa kì 1 KHTN 6")
                .score(10.0)
                .totalQuestions(1)
                .correctCount(1)
                .timeSpentSec(1800)
                .questions(List.of(
                        StudentAttemptQuestionDetailResponse.builder()
                                .questionId(100L)
                                .questionOrder(1)
                                .content("Câu hỏi 1?")
                                .point(10.0)
                                .earnedPoint(10.0)
                                .explanation("Giải thích chi tiết đáp án")
                                .selectedAnswerId(200L)
                                .isCorrect(true)
                                .answers(List.of(
                                        StudentAttemptAnswerDetailResponse.builder()
                                                .id(200L)
                                                .content("Đáp án A")
                                                .isCorrect(true)
                                                .isSelected(true)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(studentExamService.getExamAttemptDetail(eq(10L), any())).thenReturn(detailResponse);

        mockMvc.perform(get("/api/student/exam-attempts/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.attemptId").value(10))
                .andExpect(jsonPath("$.data.score").value(10.0))
                .andExpect(jsonPath("$.data.questions[0].explanation").value("Giải thích chi tiết đáp án"))
                .andExpect(jsonPath("$.data.questions[0].isCorrect").value(true))
                .andExpect(jsonPath("$.data.questions[0].answers[0].isCorrect").value(true))
                .andExpect(jsonPath("$.data.questions[0].answers[0].isSelected").value(true));

        verify(studentExamService, times(1)).getExamAttemptDetail(eq(10L), any());
    }

    @Test
    @DisplayName("GET /api/student/exam-attempts/{attemptId} - Thất bại khi không tìm thấy attempt (404)")
    void getExamAttemptDetail_NotFound() throws Exception {
        when(studentExamService.getExamAttemptDetail(eq(999L), any()))
                .thenThrow(new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        mockMvc.perform(get("/api/student/exam-attempts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(1613));

        verify(studentExamService, times(1)).getExamAttemptDetail(eq(999L), any());
    }

    @Test
    @DisplayName("GET /api/student/exam-attempts/my-history - Xem lịch sử thi thành công")
    void getMyHistory_Success() throws Exception {
        StudentExamHistoryResponse historyResponse = StudentExamHistoryResponse.builder()
                .summary(StudentHistorySummaryResponse.builder()
                        .totalExamsTaken(1)
                        .averageScore(10.0)
                        .highestScore(10.0)
                        .totalTimeSpentSec(1800L)
                        .totalCorrectQuestions(1)
                        .build())
                .attempts(List.of(
                        StudentAttemptSummaryResponse.builder()
                                .attemptId(10L)
                                .examId(1L)
                                .examCode("DE_GK1_KHTN6_01")
                                .examTitle("Đề thi giữa kì 1 KHTN 6")
                                .score(10.0)
                                .totalQuestions(1)
                                .correctCount(1)
                                .timeSpentSec(1800)
                                .submittedAt(LocalDateTime.now())
                                .build()
                ))
                .build();

        when(studentExamService.getStudentHistory(any())).thenReturn(historyResponse);

        mockMvc.perform(get("/api/student/exam-attempts/my-history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.summary.totalExamsTaken").value(1))
                .andExpect(jsonPath("$.data.summary.averageScore").value(10.0))
                .andExpect(jsonPath("$.data.attempts[0].attemptId").value(10));

        verify(studentExamService, times(1)).getStudentHistory(any());
    }
}
