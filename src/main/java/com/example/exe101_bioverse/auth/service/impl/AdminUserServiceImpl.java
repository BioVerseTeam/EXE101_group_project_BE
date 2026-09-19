package com.example.exe101_bioverse.auth.service.impl;

import com.example.exe101_bioverse.auth.dto.request.AdminUpdateUserRequest;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.entity.Role;
import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.mapper.UserMapper;
import com.example.exe101_bioverse.auth.repository.AdminUserQueryRepository;
import com.example.exe101_bioverse.auth.repository.RoleRepository;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.auth.repository.UserSessionRepository;
import com.example.exe101_bioverse.auth.repository.UserSpecifications;
import com.example.exe101_bioverse.auth.service.AdminUserService;
import com.example.exe101_bioverse.auth.service.JwtService;
import com.example.exe101_bioverse.auth.service.TokenBlacklistService;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final String ADMIN_ROLE = "ADMIN";

    private final UserRepository userRepository;
    private final AdminUserQueryRepository adminUserQueryRepository;
    private final RoleRepository roleRepository;
    private final UserSessionRepository userSessionRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AdminUserServiceImpl(
            UserRepository userRepository,
            AdminUserQueryRepository adminUserQueryRepository,
            RoleRepository roleRepository,
            UserSessionRepository userSessionRepository,
            TokenBlacklistService tokenBlacklistService,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.adminUserQueryRepository = adminUserQueryRepository;
        this.roleRepository = roleRepository;
        this.userSessionRepository = userSessionRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> listUsers(String q, UserStatus status, String role, Pageable pageable) {
        try {
            return adminUserQueryRepository.search(q, status, role, pageable);
        } catch (RuntimeException ex) {
            log.error("JDBC admin user list failed, falling back to JPA criteria: {}", ex.getMessage());
            return PageResponse.from(
                    userRepository
                            .findAll(UserSpecifications.adminList(q, status, role), pageable)
                            .map(userMapper::toResponse)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userMapper.toResponse(requireUser(id));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, AdminUpdateUserRequest request, Long adminUserId) {
        User user = requireUser(id);
        String currentRole = user.getRole().getCode();
        UserStatus currentStatus = user.getStatus();

        String nextRole = request.getRole() == null ? currentRole : request.getRole().trim().toUpperCase();
        UserStatus nextStatus = request.getStatus() == null ? currentStatus : request.getStatus();

        if (user.getId().equals(adminUserId) && (!currentRole.equals(nextRole) || currentStatus != nextStatus)) {
            throw new AppException(ErrorCode.CANNOT_MODIFY_OWN_ACCOUNT);
        }

        assertNotLastActiveAdmin(currentRole, currentStatus, nextRole, nextStatus);

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String email = request.getEmail().trim().toLowerCase();
            if (!email.equals(user.getEmail()) && userRepository.existsByEmailAndIdNot(email, user.getId())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            user.setEmail(email);
        }
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
        if (request.getEmailVerified() != null) {
            user.setEmailVerified(request.getEmailVerified());
        }
        if (request.getRole() != null) {
            Role role = roleRepository.findByCode(nextRole)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_CONFIGURED));
            user.setRole(role);
        }
        if (request.getStatus() != null) {
            user.setStatus(nextStatus);
        }

        user.setUpdatedAt(LocalDateTime.now(VN_ZONE));
        user = userRepository.save(user);

        boolean roleChanged = request.getRole() != null && !currentRole.equals(nextRole);
        boolean statusChanged = request.getStatus() != null && currentStatus != nextStatus;
        if (roleChanged || (statusChanged && nextStatus != UserStatus.ACTIVE)) {
            revokeSessions(user.getId());
        }

        return userMapper.toResponse(user);
    }

    private User requireUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void assertNotLastActiveAdmin(
            String currentRole,
            UserStatus currentStatus,
            String nextRole,
            UserStatus nextStatus
    ) {
        boolean currentlyActiveAdmin = ADMIN_ROLE.equals(currentRole) && currentStatus == UserStatus.ACTIVE;
        if (!currentlyActiveAdmin) {
            return;
        }
        boolean remainsActiveAdmin = ADMIN_ROLE.equals(nextRole) && nextStatus == UserStatus.ACTIVE;
        if (remainsActiveAdmin) {
            return;
        }
        if (userRepository.countByRole_CodeAndStatus(ADMIN_ROLE, UserStatus.ACTIVE) <= 1) {
            throw new AppException(ErrorCode.LAST_ACTIVE_ADMIN);
        }
    }

    private void revokeSessions(Long userId) {
        userSessionRepository.deleteByUserId(userId);
        tokenBlacklistService.invalidateAllUserTokens(userId, jwtService.getRefreshTokenExpirationMs());
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
