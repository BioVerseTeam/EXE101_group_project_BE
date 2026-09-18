package com.example.exe101_bioverse.auth.service.impl;

import com.example.exe101_bioverse.auth.dto.request.ChangePasswordRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateProfileRequest;
import com.example.exe101_bioverse.auth.dto.response.OtpSentResponse;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.OtpPurpose;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.mapper.UserMapper;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.auth.repository.UserSessionRepository;
import com.example.exe101_bioverse.auth.service.JwtService;
import com.example.exe101_bioverse.auth.service.MailService;
import com.example.exe101_bioverse.auth.service.OtpService;
import com.example.exe101_bioverse.auth.service.ProfileService;
import com.example.exe101_bioverse.auth.service.TokenBlacklistService;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final MailService mailService;
    private final UserSessionRepository userSessionRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;

    public ProfileServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            MailService mailService,
            UserSessionRepository userSessionRepository,
            TokenBlacklistService tokenBlacklistService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.mailService = mailService;
        this.userSessionRepository = userSessionRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        return userMapper.toResponse(requireActiveUser(userId));
    }

    @Override
    @Transactional
    public UserResponse updateMe(Long userId, UpdateProfileRequest request) {
        User user = requireActiveUser(userId);

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getPhone() != null) {
            String phone = blankToNull(request.getPhone());
            if (phone != null && userRepository.existsByPhoneAndIdNot(phone, user.getId())) {
                throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
            }
            user.setPhone(phone);
        }
        if (request.getGrade() != null) {
            user.setGrade(request.getGrade());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(blankToNull(request.getAvatarUrl()));
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        user.setUpdatedAt(LocalDateTime.now(VN_ZONE));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public OtpSentResponse sendChangePasswordOtp(Long userId) {
        User user = requireActiveUser(userId);
        String otp = otpService.issueOtp(user.getEmail(), OtpPurpose.CHANGE_PASSWORD);
        mailService.sendOtp(
                user.getEmail(),
                user.getFullName(),
                otp,
                OtpPurpose.CHANGE_PASSWORD,
                otpService.getExpireMinutes()
        );
        otpService.markCooldown(user.getEmail(), OtpPurpose.CHANGE_PASSWORD);
        return OtpSentResponse.builder()
                .email(user.getEmail())
                .purpose(OtpPurpose.CHANGE_PASSWORD)
                .expiresInSeconds(otpService.getTtlSeconds())
                .resendAfterSeconds(otpService.getResendCooldownSeconds())
                .build();
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = requireActiveUser(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_DATA, "Mật khẩu mới phải khác mật khẩu hiện tại");
        }

        otpService.verifyAndConsume(user.getEmail(), request.getOtp().trim(), OtpPurpose.CHANGE_PASSWORD);

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now(VN_ZONE));
        userRepository.save(user);

        userSessionRepository.deleteByUserId(user.getId());
        tokenBlacklistService.invalidateAllUserTokens(user.getId(), jwtService.getRefreshTokenExpirationMs());
    }

    private User requireActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        return user;
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
