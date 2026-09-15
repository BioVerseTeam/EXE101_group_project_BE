package com.example.exe101_bioverse.auth.service.impl;

import com.example.exe101_bioverse.auth.dto.request.LoginRequest;
import com.example.exe101_bioverse.auth.dto.request.LogoutRequest;
import com.example.exe101_bioverse.auth.dto.request.RefreshTokenRequest;
import com.example.exe101_bioverse.auth.dto.request.RegisterRequest;
import com.example.exe101_bioverse.auth.dto.response.AuthResponse;
import com.example.exe101_bioverse.auth.entity.Role;
import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.entity.UserSession;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.mapper.UserMapper;
import com.example.exe101_bioverse.auth.repository.RoleRepository;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.auth.repository.UserSessionRepository;
import com.example.exe101_bioverse.auth.service.AuthService;
import com.example.exe101_bioverse.auth.service.JwtService;
import com.example.exe101_bioverse.auth.service.TokenBlacklistService;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final String STUDENT_ROLE = "STUDENT";
    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserMapper userMapper;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserSessionRepository userSessionRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TokenBlacklistService tokenBlacklistService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userSessionRepository = userSessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role studentRole = roleRepository.findByCode(STUDENT_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_CONFIGURED));

        LocalDateTime now = LocalDateTime.now(VN_ZONE);
        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .phone(blankToNull(request.getPhone()))
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .role(studentRole)
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        user = userRepository.save(user);
        return issueTokens(user, httpRequest);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        String email = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> invalidCredentials());

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }

        user.setLastLoginAt(LocalDateTime.now(VN_ZONE));
        user.setUpdatedAt(LocalDateTime.now(VN_ZONE));
        user = userRepository.save(user);

        return issueTokens(user, httpRequest);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request, HttpServletRequest httpRequest) {
        UserSession session = userSessionRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (session.getExpiresAt().isBefore(LocalDateTime.now(VN_ZONE))) {
            userSessionRepository.delete(session);
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = session.getUser();
        if (user.getStatus() != UserStatus.ACTIVE) {
            userSessionRepository.delete(session);
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }

        userSessionRepository.delete(session);
        return issueTokens(user, httpRequest);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request, HttpServletRequest httpRequest) {
        String accessToken = requireBearerToken(httpRequest);
        Claims claims = parseAccessClaims(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        UserSession session = userSessionRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!session.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        userSessionRepository.delete(session);
        tokenBlacklistService.blacklistAccessToken(accessToken, claims);
    }

    @Override
    @Transactional
    public void logoutAll(HttpServletRequest httpRequest) {
        String accessToken = requireBearerToken(httpRequest);
        Claims claims = parseAccessClaims(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        userSessionRepository.deleteByUserId(userId);
        tokenBlacklistService.blacklistAccessToken(accessToken, claims);
        tokenBlacklistService.invalidateAllUserTokens(userId, jwtService.getRefreshTokenExpirationMs());
    }

    private AuthResponse issueTokens(User user, HttpServletRequest httpRequest) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(VN_ZONE);

        UserSession session = UserSession.builder()
                .user(user)
                .refreshToken(refreshToken)
                .deviceInfo(truncate(httpRequest.getHeader("User-Agent"), 500))
                .ipAddress(resolveClientIp(httpRequest))
                .expiresAt(now.plus(jwtService.getRefreshTokenExpirationMs(), ChronoUnit.MILLIS))
                .createdAt(now)
                .build();
        userSessionRepository.save(session);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TOKEN_TYPE)
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .user(userMapper.toResponse(user))
                .build();
    }

    private String requireBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.MISSING_ACCESS_TOKEN);
        }
        return header.substring(7);
    }

    private Claims parseAccessClaims(String accessToken) {
        try {
            Claims claims = jwtService.parseClaimsAllowExpired(accessToken);
            if (!"access".equals(claims.get("type", String.class))) {
                throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
            }
            return claims;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    private AppException invalidCredentials() {
        return new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
