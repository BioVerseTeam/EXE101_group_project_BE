package com.example.exe101_bioverse.auth.mapper;

import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.code")
    @Mapping(target = "currentStreak", ignore = true)
    @Mapping(target = "longestStreak", ignore = true)
    @Mapping(target = "lastCheckInDate", ignore = true)
    @Mapping(target = "checkedInToday", ignore = true)
    UserResponse toResponse(User user);
}
