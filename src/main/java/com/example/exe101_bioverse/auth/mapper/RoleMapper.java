package com.example.exe101_bioverse.auth.mapper;

import com.example.exe101_bioverse.auth.dto.response.RoleResponse;
import com.example.exe101_bioverse.auth.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toResponse(Role role);
}
