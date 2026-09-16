package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.dto.request.CreateRoleRequest;
import com.example.exe101_bioverse.auth.dto.request.UpdateRoleRequest;
import com.example.exe101_bioverse.auth.dto.response.RoleResponse;
import com.example.exe101_bioverse.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    PageResponse<RoleResponse> listRoles(String q, Pageable pageable);

    RoleResponse getRole(Long id);

    RoleResponse createRole(CreateRoleRequest request);

    RoleResponse updateRole(Long id, UpdateRoleRequest request);

    void deleteRole(Long id);
}
