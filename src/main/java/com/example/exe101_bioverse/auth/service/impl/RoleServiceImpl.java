package com.example.exe101_bioverse.auth.service.impl;

import com.example.exe101_bioverse.auth.dto.request.CreateRoleRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateRoleRequest;
import com.example.exe101_bioverse.auth.dto.response.RoleResponse;
import com.example.exe101_bioverse.auth.entity.Role;
import com.example.exe101_bioverse.auth.mapper.RoleMapper;
import com.example.exe101_bioverse.auth.repository.RoleRepository;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.auth.repository.UserSessionRepository;
import com.example.exe101_bioverse.auth.service.JwtService;
import com.example.exe101_bioverse.auth.service.RoleService;
import com.example.exe101_bioverse.auth.service.TokenBlacklistService;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final Set<String> SYSTEM_ROLE_CODES = Set.of("ADMIN", "STUDENT");

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            UserRepository userRepository,
            UserSessionRepository userSessionRepository,
            TokenBlacklistService tokenBlacklistService,
            JwtService jwtService,
            RoleMapper roleMapper
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtService = jwtService;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleResponse> listRoles(String q, Pageable pageable) {
        String query = blankToNull(q);
        Page<Role> page = query == null
                ? roleRepository.findAll(pageable)
                : roleRepository.search(query, pageable);
        return PageResponse.from(page.map(roleMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRole(Long id) {
        return roleMapper.toResponse(requireRole(id));
    }

    @Override
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        String code = normalizeCode(request.getCode());
        if (roleRepository.existsByCode(code)) {
            throw new AppException(ErrorCode.ROLE_CODE_ALREADY_EXISTS);
        }

        Role role = Role.builder()
                .code(code)
                .name(request.getName().trim())
                .description(blankToNull(request.getDescription()))
                .createdAt(LocalDateTime.now(VN_ZONE))
                .build();

        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, UpdateRoleRequest request) {
        Role role = requireRole(id);
        String currentCode = role.getCode();
        boolean codeChanged = false;

        if (request.getCode() != null && !request.getCode().isBlank()) {
            String nextCode = normalizeCode(request.getCode());
            if (!currentCode.equals(nextCode)) {
                assertNotSystemRole(currentCode);
                if (roleRepository.existsByCodeAndIdNot(nextCode, role.getId())) {
                    throw new AppException(ErrorCode.ROLE_CODE_ALREADY_EXISTS);
                }
                role.setCode(nextCode);
                codeChanged = true;
            }
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            role.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            role.setDescription(blankToNull(request.getDescription()));
        }

        role = roleRepository.save(role);

        if (codeChanged) {
            revokeSessionsForRole(role.getId());
        }

        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = requireRole(id);
        assertNotSystemRole(role.getCode());
        if (userRepository.existsByRole_Id(role.getId())) {
            throw new AppException(ErrorCode.ROLE_IN_USE);
        }
        roleRepository.delete(role);
    }

    private Role requireRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private void assertNotSystemRole(String code) {
        if (SYSTEM_ROLE_CODES.contains(code)) {
            throw new AppException(ErrorCode.SYSTEM_ROLE_PROTECTED);
        }
    }

    private void revokeSessionsForRole(Long roleId) {
        List<Long> userIds = userRepository.findIdsByRoleId(roleId);
        long ttlMs = jwtService.getRefreshTokenExpirationMs();
        for (Long userId : userIds) {
            userSessionRepository.deleteByUserId(userId);
            tokenBlacklistService.invalidateAllUserTokens(userId, ttlMs);
        }
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
