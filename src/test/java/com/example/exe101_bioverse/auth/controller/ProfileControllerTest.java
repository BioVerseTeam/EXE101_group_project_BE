package com.example.exe101_bioverse.auth.controller;

import com.example.exe101_bioverse.auth.dto.request.ChangePasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateProfileRequest;
import com.example.exe101_bioverse.auth.dto.response.OtpSentResponse;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.enums.GenderType;
import com.example.exe101_bioverse.auth.enums.OtpPurpose;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.security.UserPrincipal;
import com.example.exe101_bioverse.auth.service.ProfileService;
import com.example.exe101_bioverse.common.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private final UserPrincipal mockPrincipal = new UserPrincipal(
            1L,
            "student@bioverse.vn",
            "hashedpassword",
            "STUDENT",
            true
    );

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HandlerMethodArgumentResolver principalResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().isAssignableFrom(UserPrincipal.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return mockPrincipal;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setCustomArgumentResolvers(principalResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/users/me - Lấy thông tin cá nhân thành công")
    void getMe_Success() throws Exception {
        UserResponse mockResponse = UserResponse.builder()
                .id(1L)
                .email("student@bioverse.vn")
                .fullName("Nguyễn Văn An")
                .phone("0912345678")
                .grade(8)
                .gender(GenderType.MALE)
                .role("STUDENT")
                .status(UserStatus.ACTIVE)
                .currentStreak(5)
                .longestStreak(10)
                .checkedInToday(true)
                .build();

        when(profileService.getMe(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("student@bioverse.vn"))
                .andExpect(jsonPath("$.data.fullName").value("Nguyễn Văn An"))
                .andExpect(jsonPath("$.data.grade").value(8))
                .andExpect(jsonPath("$.data.currentStreak").value(5))
                .andExpect(jsonPath("$.data.checkedInToday").value(true));

        verify(profileService, times(1)).getMe(1L);
    }

    @Test
    @DisplayName("PATCH /api/users/me - Chỉnh sửa thông tin cá nhân thành công")
    void updateMe_Success() throws Exception {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .fullName("Nguyễn Văn Bình")
                .phone("0987654321")
                .grade(9)
                .gender(GenderType.MALE)
                .avatarUrl("https://storage.bioverse.edu.vn/avatars/new-avatar.png")
                .dateOfBirth(LocalDate.of(2010, 5, 20))
                .build();

        UserResponse updatedResponse = UserResponse.builder()
                .id(1L)
                .email("student@bioverse.vn")
                .fullName("Nguyễn Văn Bình")
                .phone("0987654321")
                .grade(9)
                .gender(GenderType.MALE)
                .avatarUrl("https://storage.bioverse.edu.vn/avatars/new-avatar.png")
                .dateOfBirth(LocalDate.of(2010, 5, 20))
                .build();

        when(profileService.updateMe(eq(1L), any(UpdateProfileRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.fullName").value("Nguyễn Văn Bình"))
                .andExpect(jsonPath("$.data.phone").value("0987654321"))
                .andExpect(jsonPath("$.data.grade").value(9))
                .andExpect(jsonPath("$.data.avatarUrl").value("https://storage.bioverse.edu.vn/avatars/new-avatar.png"));

        verify(profileService, times(1)).updateMe(eq(1L), any(UpdateProfileRequest.class));
    }

    @Test
    @DisplayName("POST /api/users/me/password/otp - Gửi OTP đổi mật khẩu thành công")
    void sendChangePasswordOtp_Success() throws Exception {
        OtpSentResponse mockOtpResponse = OtpSentResponse.builder()
                .email("student@bioverse.vn")
                .purpose(OtpPurpose.CHANGE_PASSWORD)
                .expiresInSeconds(300)
                .resendAfterSeconds(60)
                .build();

        when(profileService.sendChangePasswordOtp(1L)).thenReturn(mockOtpResponse);

        mockMvc.perform(post("/api/users/me/password/otp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Đã gửi mã OTP đến email"))
                .andExpect(jsonPath("$.data.email").value("student@bioverse.vn"))
                .andExpect(jsonPath("$.data.expiresInSeconds").value(300));

        verify(profileService, times(1)).sendChangePasswordOtp(1L);
    }

    @Test
    @DisplayName("POST /api/users/me/password - Đổi mật khẩu thành công")
    void changePassword_Success() throws Exception {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("OldPass123!")
                .newPassword("NewPass456!")
                .confirmPassword("NewPass456!")
                .otp("123456")
                .build();

        doNothing().when(profileService).changePassword(eq(1L), any(ChangePasswordRequest.class));

        mockMvc.perform(post("/api/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Đổi mật khẩu thành công, vui lòng đăng nhập lại"));

        verify(profileService, times(1)).changePassword(eq(1L), any(ChangePasswordRequest.class));
    }
}
